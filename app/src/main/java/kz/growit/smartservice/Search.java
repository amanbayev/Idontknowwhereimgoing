package kz.growit.smartservice;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mikepenz.materialdrawer.Drawer;
import com.rey.material.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.FragmentPagerAdapter;
import kz.growit.smartservice.Models.City;
import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.Models.ServiceCategory;
import kz.growit.smartservice.Models.Specialization;


public class Search extends AppCompatActivity {
    private Toolbar toolbar;
    private Drawer drawer;

    private com.rey.material.widget.Spinner region, city, category, specialization;

    private ArrayList<String> reks = new ArrayList<>();
    private ArrayList<String> cats = new ArrayList<>();
    private ArrayList<String> cits = new ArrayList<>();
    private ArrayList<String> specs = new ArrayList<>();

    private int selectedRegId, selectedCatId, selectedSpecId, selectedCitId = 0;

    private ArrayList<ServiceCategory> kats = AppController.getInstance().getCategoriesArray();
    private ArrayList<Region> regs = AppController.getInstance().getRegionsArray();
    private ArrayList<City> czits;
    private ArrayList<Specialization> speks;

    private ArrayAdapter<String> catsAdapter, regsAdapter, citsAdapter, specsAdapter;

    private EditText queryEditText;
    private com.dd.CircularProgressButton searchButton;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        toolbar.setTitle(getResources().getString(R.string.search_ru));
        AppController.getInstance().setRecentSearch(new ArrayList<SearchResultItem>());
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        drawer = AppController.getInstance().getDrawer(Search.this, toolbar);

        queryEditText = (EditText) findViewById(R.id.searchQueryEditTextSearch);
        searchButton = (com.dd.CircularProgressButton) findViewById(R.id.searchButtonSearch);

        region = (Spinner) findViewById(R.id.regionSpinnerSearch);
        city = (Spinner) findViewById(R.id.citySpinnerSearch);
        category = (Spinner) findViewById(R.id.categorySpinnerSearch);
        specialization = (Spinner) findViewById(R.id.specializationSpinnerSearch);

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchButton.getProgress() != 0) {
                    searchButton.setProgress(0);
                    queryEditText.setEnabled(true);
                } else {
                    searchNow();
                    searchButton.setIndeterminateProgressMode(true);
                    searchButton.setProgress(50);
                    queryEditText.setEnabled(false);
                }
            }
        });
        updateListView();
        searchNow();
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

    private void updateListView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                Search.this));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void searchNow() {
        String url = "http://smartservice.kz/api/UserProfilesApi/SearchUserProfiles?SearchQuery=" +
                queryEditText.getText().toString().trim() +
                "&CityId=" + selectedCitId +
                "&RegionId=" + selectedRegId +
                "&ServiceCategoryId=" + selectedCatId;
        url = "http://smartservice.kz/api/UserProfilesApi/SearchUserProfiles?SearchQuery=&CityId=1&RegionId=1&ServiceCategoryId=19";
        JSONObject params = new JSONObject();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Snackbar.make(findViewById(R.id.viewpager), getResources().getString(R.string.search_outro_ru), Snackbar.LENGTH_LONG).show();
                        ArrayList<SearchResultItem> searchResults = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                SearchResultItem temp = new SearchResultItem(response.getJSONObject(i));
                                searchResults.add(temp);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (AppController.getInstance().getRecentSearch() != null)
                            searchButton.setProgress(100);
                        AppController.getInstance().setRecentSearch(searchResults);
                        android.support.v7.widget.RecyclerView recyclerView = (android.support.v7.widget.RecyclerView) viewPager.findViewById(R.id.recyclerViewSearchFragmentTwo);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        // no layout manager error!


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("search querry", error.toString());
                        Snackbar.make(findViewById(R.id.viewpager), getResources().getString(R.string.search_error_ru), Snackbar.LENGTH_LONG).show();
                        searchButton.setProgress(-1);
                    }
                }
        );
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
    }

}
