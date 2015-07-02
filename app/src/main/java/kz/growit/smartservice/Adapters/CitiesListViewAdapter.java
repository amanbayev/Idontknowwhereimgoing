package kz.growit.smartservice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kz.growit.smartservice.Models.City;
import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 02.07.2015.
 */
public class CitiesListViewAdapter extends BaseAdapter {
    private ArrayList<City> regions;
    private LayoutInflater inflater;
    private Activity activity;
    boolean citiesB = false;

    public CitiesListViewAdapter(Activity activity, ArrayList<City> data) {
        this.regions = data;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return regions.size();
    }

    @Override
    public Object getItem(int position) {
        return regions.get(position).getDescription();
    }

    @Override
    public long getItemId(int position) {
        return regions.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.region_select_row, null);
        TextView regionName = (TextView) convertView.findViewById(R.id.regionNameTextViewRegionSelectRow);

        regionName.setText(regions.get(position).getDescription());

        return convertView;
    }
}
