package kz.growit.smartservice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.CitiesListViewAdapter;
import kz.growit.smartservice.Adapters.RegionsListViewAdapter;
import kz.growit.smartservice.Models.City;


public class SelectCity extends AppCompatActivity {
    private ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSelectCity);
        setSupportActionBar(toolbar);

        cities = AppController.getInstance().getCitiesForRegion(AppController.getInstance().getSelectedRegionId());
        ListView citiesLV = (ListView) findViewById(R.id.cityListViewSelectCity);
        CitiesListViewAdapter myAdapter = new CitiesListViewAdapter(SelectCity.this, cities);
        citiesLV.setAdapter(myAdapter);
        citiesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppController.getInstance().setSelectedCityId(cities.get(position).getId());
                Intent goToSearchNewDesign = new Intent(SelectCity.this, SearchNewDesign.class);
                goToSearchNewDesign.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToSearchNewDesign);
                finish();
            }
        });
    }

}
