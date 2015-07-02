package kz.growit.smartservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.RegionsListViewAdapter;
import kz.growit.smartservice.Models.Region;


public class SelectRegion extends AppCompatActivity {
    private ArrayList<Region> regions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSelectRegion);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.select_region_ru));
        regions = AppController.getInstance().getRegionsArray();
        final ListView regionsLV = (ListView) findViewById(R.id.regionsListViewSelectRegion);
        RegionsListViewAdapter myAdapter = new RegionsListViewAdapter(SelectRegion.this, regions);
        regionsLV.setAdapter(myAdapter);
        regionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(regionsLV, String.valueOf(regions.get(position).getId()), Snackbar.LENGTH_LONG).show();
                AppController.getInstance().setSelectedRegionId(regions.get(position).getId());
                Intent goToSelectCity = new Intent(SelectRegion.this, SelectCity.class);
                goToSelectCity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToSelectCity);
                finish();
            }
        });
    }


}
