package kz.growit.smartservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.CheckBoxListViewAdapter;
import kz.growit.smartservice.Models.City;
import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.ServiceCategory;
import kz.growit.smartservice.Models.Specialization;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, password, phone, name, otherPhones, education, experience, about;
    private com.rey.material.widget.Spinner city, region, category, specialization;
    private ListView distrcits;
    private Button sendForm;
    private ArrayList<String> reks = new ArrayList<>();
    private ArrayList<String> cats = new ArrayList<>();
    private ArrayList<String> cits = new ArrayList<>();
    private ArrayList<String> specs = new ArrayList<>();
    private ArrayList<String> dists = new ArrayList<>();
    private ArrayList<ServiceCategory> kats = AppController.getInstance().getCategoriesArray();
    private ArrayList<Region> regs = AppController.getInstance().getRegionsArray();
    private ArrayList<City> czits;
    private ArrayList<Specialization> speks;
    private ArrayAdapter<String> catsAdapter, regsAdapter, citsAdapter, specsAdapter, distsAdapter;
    private int selectedRegId, selectedCatId, selectedSpecId, selectedCitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.emailRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        phone = (EditText) findViewById(R.id.phoneRegister);
        name = (EditText) findViewById(R.id.nameRegister);
        otherPhones = (EditText) findViewById(R.id.otherPhonesRegister);
        about = (EditText) findViewById(R.id.aboutRegister);
        education = (EditText) findViewById(R.id.educationRegister);
        experience = (EditText) findViewById(R.id.experienceRegister);

        region = (Spinner) findViewById(R.id.regionSpinnerRegister);
        city = (Spinner) findViewById(R.id.citySpinnerRegister);
        category = (Spinner) findViewById(R.id.categorySpinnerRegister);
        specialization = (Spinner) findViewById(R.id.specializationSpinnerRegister);
        distrcits = (ListView) findViewById(R.id.districtsRegister);
        sendForm = (Button) findViewById(R.id.sendRegistrationButton);
        initSpinners();

        sendForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate form data first
                String url = AppController.getInstance().SMART_SERVICE_URL + "api/AccountApi/Register";
                JSONObject data = new JSONObject();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String ok = "ok";
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("registration", error.getLocalizedMessage());
                            }
                        }
                );
                AppController.getInstance().addToRequestQueue(jsonObjectRequest, "registration");
            }
        });

    }

    private void initSpinners() {
        for (int i = 0; i < kats.size(); i++) {
            cats.add(kats.get(i).getDescription());
        }
        for (int i = 0; i < regs.size(); i++) {
            reks.add(regs.get(i).getDescription());
        }

        // init districts need to remove hardCode
        dists.add("Есильский");
        dists.add("Сарыаркинский");
        dists.add("Алматинский");


        selectedCatId = kats.get(0).getId();
        selectedRegId = regs.get(0).getId();
        czits = AppController.getInstance().getCitiesForRegion(selectedRegId);
        for (int i = 0; i < czits.size(); i++) {
            cits.add(czits.get(i).getDescription());
        }
        selectedCitId = czits.get(0).getId();

        speks = AppController.getInstance().getSpecializationsForServiceCategory(selectedCatId);

        for (int i = 0; i < speks.size(); i++) {
            specs.add(speks.get(i).getDescription());
        }

        selectedSpecId = speks.get(0).getId();

        CheckBoxListViewAdapter myAdapter = new CheckBoxListViewAdapter(this, getApplicationContext(), R.id.districtsRegister, dists);

        distrcits.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 65 * dists.size()));
        distrcits.setAdapter(myAdapter);


        catsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cats);
        category.setAdapter(catsAdapter);

        regsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reks);
        region.setAdapter(regsAdapter);

        specsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specs);
        specialization.setAdapter(specsAdapter);

        citsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cits);
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
