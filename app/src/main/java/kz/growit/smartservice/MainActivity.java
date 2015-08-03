package kz.growit.smartservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.ServiceCategory;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private TextView status;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String SENDER_ID = "444280764221";
    private static final String TAG = "GCM User Registration";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.statusTextSplashScreenTextView);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            status.setText(R.string.connecting_to_server_ru);
            String url = "http://smartservice.kz/api/ServiceRequestsApi/GetNewRequestData";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    new JSONObject(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            HashMap<Integer, Region> regions = new HashMap<>();
                            HashMap<Integer, ServiceCategory> categories = new HashMap<>();
                            try {
                                JSONArray jsRegions = response.getJSONArray("Regions");
                                status.setText(R.string.loading_regions_ru);
                                for (int i = 0; i < jsRegions.length(); i++) {
                                    Region tempRegion = new Region(jsRegions.getJSONObject(i));
                                    regions.put(tempRegion.getId(), tempRegion);
                                }
                                status.setText(R.string.loading_categories_ru);
                                JSONArray jsCategories = response.getJSONArray("ServiceCategories");
                                for (int i = 0; i < jsCategories.length(); i++) {
                                    ServiceCategory tempCat = new ServiceCategory(jsCategories.getJSONObject(i));
                                    categories.put(tempCat.getId(), tempCat);
                                }
                                AppController.getInstance().setRegions(regions);
                                AppController.getInstance().setServiceCategories(categories);
                                regid = getRegistrationId(getApplicationContext());
                                if (regid.equals("")) {
                                    status.setText(R.string.loading_regid_ru);
                                    registerInBackground();
                                } else {
                                    AppController.getInstance().setRegid(regid);
                                    new Handler().postDelayed(
                                            new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent goToLargeButtons = new Intent(MainActivity.this, LargeButtons.class);
                                                    goToLargeButtons.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(goToLargeButtons);
                                                    finish();
                                                }
                                            },
                                            SPLASH_TIME_OUT
                                    );
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("init error", error.getMessage().toString());
                        }
                    }
            );
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(jsonObjectRequest, "init");
        } else {
            Snackbar
                    .make(findViewById(R.id.mainActivityLayout), R.string.no_internet_ru, Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action_close_ru, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("regid", regid);

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    //sendRegistrationIdToBackend();

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getApplicationContext(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                AppController.getInstance().setRegid(regid);
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Intent goToLargeButtons = new Intent(MainActivity.this, LargeButtons.class);
                                goToLargeButtons.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(goToLargeButtons);
                                finish();
                            }
                        },
                        SPLASH_TIME_OUT
                );
            }
        }.execute(null, null, null);
    }

    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);

        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
