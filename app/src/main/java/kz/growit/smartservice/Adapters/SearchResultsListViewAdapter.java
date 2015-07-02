package kz.growit.smartservice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kz.growit.smartservice.AppController;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 02.07.2015.
 */
public class SearchResultsListViewAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<SearchResultItem> results;
    private LayoutInflater inflater;

    public SearchResultsListViewAdapter(Activity activity, ArrayList<SearchResultItem> sResults) {
        this.activity = activity;
        this.results = sResults;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public SearchResultItem getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return results.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.search_result_recycler_view_item_row, null);

        com.android.volley.toolbox.NetworkImageView thumb = (com.android.volley.toolbox.NetworkImageView) convertView.findViewById(R.id.thumbnailSearchItemRow);
        ArrayList<ImageView> stars = new ArrayList<>();
        stars.add((ImageView) convertView.findViewById(R.id.starOneSearchResultRow));
        stars.add((ImageView) convertView.findViewById(R.id.starTwoSearchResultRow));
        stars.add((ImageView) convertView.findViewById(R.id.starThreeSearchResultRow));
        stars.add((ImageView) convertView.findViewById(R.id.starFourSearchResultRow));
        stars.add((ImageView) convertView.findViewById(R.id.starFiveSearchResultRow));
        TextView nameSurname = (TextView) convertView.findViewById(R.id.nameSuernameSearchItemRow);
        TextView lastUpdate = (TextView) convertView.findViewById(R.id.lastUpdatedTextView);

        nameSurname.setText(results.get(position).getNameSurname());
        lastUpdate.setText(AppController.getInstance().daysFromDate(results.get(position).getProfileUpdateDate()));
        for (int i = 0; i < results.get(position).getRating(); i++) {
            stars.get(i).setImageDrawable(activity.getResources().getDrawable(R.drawable.star));
        }
        thumb.setImageUrl("http://smartservice.kz" + results.get(position).getProfileImageUrl(), AppController.getInstance().getImageLoader());
        return convertView;
    }
}
