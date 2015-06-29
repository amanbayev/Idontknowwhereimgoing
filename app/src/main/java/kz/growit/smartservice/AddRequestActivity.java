package kz.growit.smartservice;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.rey.material.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kz.growit.smartservice.Models.City;
import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.ServiceCategory;
import kz.growit.smartservice.Models.Specialization;


public class AddRequestActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Drawer drawer;
    private com.dd.CircularProgressButton addRequestButton;
    private Spinner region, city, category, specialization;
    private int selectedRegId, selectedCatId, selectedSpecId, selectedCitId = 0;
    private ArrayList<Region> regs = AppController.getInstance().getRegionsArray();
    private ArrayList<String> reks = new ArrayList<>();
    private ArrayList<String> cats = new ArrayList<>();
    private ArrayList<String> cits = new ArrayList<>();
    private ArrayList<String> specs = new ArrayList<>();
    private ArrayList<City> czits;
    private ArrayList<Specialization> speks;
    private AutoCompleteTextView email;
    private android.widget.EditText name, phone, request;
    private ArrayAdapter<String> catsAdapter, regsAdapter, citsAdapter, specsAdapter;
    private boolean OK = false;
    private boolean done = false;
    private ArrayList<ServiceCategory> kats = AppController.getInstance().getCategoriesArray();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        toolbar = (Toolbar) findViewById(R.id.toolbarAddRequest);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        drawer = AppController.getInstance().getDrawer(AddRequestActivity.this, toolbar);

        region = (Spinner) findViewById(R.id.regionSpinnerAddRequest);
        city = (Spinner) findViewById(R.id.citySpinnerAddRequest);
        category = (Spinner) findViewById(R.id.categorySpinnerAddRequest);
        specialization = (Spinner) findViewById(R.id.specializationSpinnerAddRequest);

        name = (android.widget.EditText) findViewById(R.id.nameAddRequest);
        phone = (android.widget.EditText) findViewById(R.id.phoneAddRequest);
        email = (AutoCompleteTextView) findViewById(R.id.emailAddRequest);
        request = (EditText) findViewById(R.id.requestAddRequest);

        Account[] accounts = AccountManager.get(this).getAccounts();
        Set<String> emailSet = new HashSet<>();
        for (Account account : accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }
        }
        email.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>(emailSet)));

        addRequestButton = (com.dd.CircularProgressButton) findViewById(R.id.addButtonAddRequest);

        for (int i = 0; i < kats.size(); i++) {
            cats.add(kats.get(i).getDescription());
        }
        for (int i = 0; i < regs.size(); i++) {
            reks.add(regs.get(i).getDescription());
        }

        selectedCatId = kats.get(0).getId();
        selectedRegId = regs.get(0).getId();

        czits = AppController.getInstance().getCitiesForRegion(selectedRegId);
        cits = new ArrayList<>();
        for (int i = 0; i < czits.size(); i++) {
            cits.add(czits.get(i).getDescription());
        }
        selectedCitId = czits.get(0).getId();

        speks = AppController.getInstance().getSpecializationsForServiceCategory(selectedCatId);

        specs = new ArrayList<>();
        for (int i = 0; i < speks.size(); i++) {
            specs.add(speks.get(i).getDescription());
        }

        selectedSpecId = speks.get(0).getId();

        catsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cats
        );
        category.setAdapter(catsAdapter);

        regsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                reks
        );
        region.setAdapter(regsAdapter);

        specsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                specs
        );
        specialization.setAdapter(specsAdapter);

        citsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                cits
        );
        city.setAdapter(citsAdapter);

        category.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                selectedCatId = kats.get(i).getId();
                updateSpecs();
                return true;
            }
        });

        region.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                selectedRegId = regs.get(i).getId();
                updateCits();
                return true;
            }
        });

        city.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                selectedCitId = czits.get(i).getId();
                return true;
            }
        });

        specialization.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                selectedSpecId = speks.get(i).getId();
                return true;
            }
        });

        request.setHint(getResources().getString(R.string.request_hint_ru) + "\n" + getResources().getString(R.string.request_2_hint_ru));
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                if (done) {
                    finish();
                }
                if (phone.getText().length() < 11) {
                    phone.setError(getResources().getString(R.string.phone_too_short_ru));
                    error = true;
                }
                if (!isEmailValid(email.getText().toString().trim())) {
                    email.setError(getResources().getString(R.string.email_format_error_ru));
                    error = true;
                }
                if (name.getText().toString().trim().length() < 1) {
                    error = true;
                    name.setError(getResources().getString(R.string.name_too_short_ru));
                }
                if (request.getText().toString().trim().length() < 10) {
                    error = true;
                    request.setError(getResources().getString(R.string.request_too_short_ru));
                }
                if (error) OK = false;
                else OK = true;
                if (OK) {
                    addRequestButton.setIndeterminateProgressMode(true);
                    addRequestButton.setProgress(50);
                    email.setEnabled(false);
                    name.setEnabled(false);
                    phone.setEnabled(false);
                    request.setEnabled(false);
                    category.setEnabled(false);
                    city.setEnabled(false);
                    region.setEnabled(false);
                    specialization.setEnabled(false);

                    String url = "http://smartservice.kz/api/ServiceRequestsApi/PostServiceRequest";
                    JSONObject params = new JSONObject();
                    try {
                        params.put("NameSurname", name.getText().toString().trim());
                        params.put("Email", email.getText().toString().trim());
                        params.put("ServiceCategoryId", selectedCatId);
                        params.put("SpecializationId", selectedSpecId);
                        params.put("RegionId", selectedRegId);
                        params.put("CityId", selectedCitId);
                        params.put("ServiceDescription", request.getText().toString().trim());
                        params.put("PhoneNumber", phone.getText().toString().trim());
                        params.put("DeviceRegistrationId", AppController.getInstance().getRegid());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //String rofl = AppController.getInstance().jsonToUrlEncodedString(params);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    addRequestButton.setProgress(100);
                                    done = true;
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    addRequestButton.setProgress(-1);
                                    Log.d("add request", error.toString());
                                    email.setEnabled(true);
                                    name.setEnabled(true);
                                    phone.setEnabled(true);
                                    request.setEnabled(true);
                                    category.setEnabled(true);
                                    city.setEnabled(true);
                                    region.setEnabled(true);
                                    specialization.setEnabled(true);
                                }
                            }
                    );
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppController.getInstance().addToRequestQueue(jsonObjectRequest, "post request");
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    private void updateCits() {
        czits = AppController.getInstance().getCitiesForRegion(selectedRegId);
        cits.clear();
        for (int i = 0; i < czits.size(); i++) {
            cits.add(czits.get(i).getDescription());
        }
        selectedCitId = czits.get(0).getId();
        citsAdapter.notifyDataSetChanged();
    }

    private void updateSpecs() {
        speks = AppController.getInstance().getSpecializationsForServiceCategory(selectedCatId);
        specs.clear();
        for (int i = 0; i < speks.size(); i++) {
            specs.add(speks.get(i).getDescription());
        }
        selectedSpecId = speks.get(0).getId();
        specsAdapter.notifyDataSetChanged();
    }
}
