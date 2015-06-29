package kz.growit.smartservice.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import kz.growit.smartservice.R;

/**
 * Created by Талгат on 29.06.2015.
 */
public class SearchResultViewHolder extends RecyclerView.ViewHolder{
    protected NetworkImageView thumb;
    protected ArrayList<ImageView> stars;
    protected TextView title, updateDate;

    public SearchResultViewHolder(View v) {
        super(v);
        thumb = (NetworkImageView) v.findViewById(R.id.thumbnailSearchItemRow);
        stars = new ArrayList<>();
        stars.add((ImageView) v.findViewById(R.id.starOneSearchResultRow));
        stars.add((ImageView) v.findViewById(R.id.starTwoSearchResultRow));
        stars.add((ImageView) v.findViewById(R.id.starThreeSearchResultRow));
        stars.add((ImageView) v.findViewById(R.id.starFourSearchResultRow));
        stars.add((ImageView) v.findViewById(R.id.starFiveSearchResultRow));
        title = (TextView) v.findViewById(R.id.nameSuernameSearchItemRow);
        updateDate = (TextView) v.findViewById(R.id.lastUpdatedTextView);
    }
}
