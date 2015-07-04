package kz.growit.smartservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.ServiceCategory;


public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private TextView status;


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
            AppController.getInstance().addToRequestQueue(jsonObjectRequest,"init");
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


}
