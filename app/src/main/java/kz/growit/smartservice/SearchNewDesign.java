package kz.growit.smartservice;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.mikepenz.materialdrawer.Drawer;
import com.rey.material.widget.Spinner;
import com.rey.material.widget.EditText;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.SearchResultsListViewAdapter;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.Models.ServiceCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SearchNewDesign extends AppCompatActivity {

    private Spinner category;
    private LinearLayout regionSelector;
    private int SelectedCityId, SelectedRegionId, SelectedCategoryId;
    private TextView cityName, regionName;
    private AppController myApp = AppController.getInstance();
    private SearchResultsListViewAdapter myAdapter;
    private ArrayList<ServiceCategory> kats = myApp.getCategoriesArray();
    private ArrayList<String> cats = new ArrayList<>();

    private ArrayAdapter<String> catsAdapter;
    private ListView searchResultsLV;
    private com.dd.CircularProgressButton searchButton;
    private EditText searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Toolbar toolbar;
        Drawer drawer;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new_design);
        toolbar = (Toolbar) findViewById(R.id.toolbarSearchNewDesign);
        toolbar.setTitle(getResources().getString(R.string.search_ru));
        AppController.getInstance().setRecentSearch(new ArrayList<SearchResultItem>());
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        SelectedRegionId = myApp.getSelectedRegionId();
        SelectedCityId = myApp.getSelectedCityId();

        cityName = (TextView) findViewById(R.id.cityNewDesignSelectorTextView);
        regionName = (TextView) findViewById(R.id.regionNewDesignSelectorTextView);

        searchButton = (com.dd.CircularProgressButton) findViewById(R.id.searchCircularButtonNewDesign);
        searchQuery = (EditText) findViewById(R.id.searchQuerryNewDesignEditText);

        if (SelectedCityId > 0) {
            String region = myApp.getRegions().get(SelectedRegionId).getDescription();
            String city = myApp.getCity(SelectedRegionId, SelectedCityId);
            //Snackbar.make(toolbar, region + " and " + city, Snackbar.LENGTH_LONG).show();
            cityName.setText(city);
            regionName.setText(region);
        }

        drawer = myApp.getDrawer(SearchNewDesign.this, toolbar);

        category = (Spinner) findViewById(R.id.categorySpinnerSearchNewDesign);

        regionSelector = (LinearLayout) findViewById(R.id.regionSelectorNewDesignSearchLL);
        regionSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSelectRegion = new Intent(SearchNewDesign.this, SelectRegion.class);
                goToSelectRegion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToSelectRegion);
            }
        });

        category = (Spinner) findViewById(R.id.categorySpinnerSearchNewDesign);

        for (int i = 0; i < kats.size(); i++) {
            cats.add(kats.get(i).getDescription());
        }


        catsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cats);
        category.setAdapter(catsAdapter);
        category.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                SelectedCategoryId = kats.get(i).getId();
                myApp.setSelectedCategoryId(SelectedCategoryId);
                //Snackbar.make(regionSelector, String.valueOf(SelectedCategoryId), Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
        SelectedCategoryId = kats.get(0).getId();
        myApp.setSelectedCategoryId(SelectedCategoryId);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setIndeterminateProgressMode(true);
                searchButton.setProgress(50);
                searchNow();
            }
        });

        searchQuery.clearFocus();

        searchResultsLV = (ListView) findViewById(R.id.searchResultsListView);
        searchResultsLV.setDividerHeight(0);
        myAdapter = new SearchResultsListViewAdapter(SearchNewDesign.this, myApp.getRecentSearch());
        searchResultsLV.setAdapter(myAdapter);
        searchResultsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable drawable;
                NetworkImageView thumb = (NetworkImageView) view.findViewById(R.id.thumbnailSearchItemRow);
                try {
                    drawable = thumb.getDrawable();
                    myApp.setProfileDrawable(drawable);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                //Snackbar.make(view, sri.getNameSurname(), Snackbar.LENGTH_LONG).show();
                Intent goToProfileDetailView = new Intent(SearchNewDesign.this, ProfileDetailView.class);
                myApp.setSelectedProfileId(myApp.getRecentSearch().get(position).getId());
                goToProfileDetailView.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToProfileDetailView);
            }
        });
    }

    private void searchNow() {
        String url = "http://smartservice.kz/api/UserProfilesApi/SearchUserProfiles?SearchQuery=" +
                searchQuery.getText().toString().trim() +
                "&CityId=" + SelectedCityId +
                "&RegionId=" + SelectedRegionId +
                "&ServiceCategoryId=" + SelectedCategoryId;

        JSONObject params = new JSONObject();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                params,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
                            searchButton.setProgress(0);
                        AppController.getInstance().setRecentSearch(searchResults);
                        myAdapter = new SearchResultsListViewAdapter(SearchNewDesign.this, myApp.getRecentSearch());
                        searchResultsLV.setAdapter(myAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("search querry", error.toString());
                        Snackbar.make(findViewById(R.id.searchQuerryNewDesignEditText), getResources().getString(R.string.search_error_ru), Snackbar.LENGTH_LONG).show();
                        searchButton.setProgress(-1);
                    }
                }
        );
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, "search");
    }
}
