package kz.growit.smartservice.Adapters;

import android.content.Context;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import kz.growit.smartservice.AppController;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 29.06.2015.
 */
public class SearchResultRecyclerViewItemAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewItemAdapter.SearchResultRecyclerViewHolder> {
    private Context context;
    private List<SearchResultItem> results;
    private int lastPosition = -1;

    public SearchResultRecyclerViewItemAdapter(Context context, List<SearchResultItem> passedInArray) {
        this.context = context;
        this.results = passedInArray;
    }

    @Override
    public SearchResultRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_recycler_view_item_row, parent);
        return new SearchResultRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchResultRecyclerViewHolder holder, int position) {
        SearchResultItem item = results.get(position);
        holder.thumb.setImageUrl("http://smartservice.kz/" + item.getProfileImageUrl(), AppController.getInstance().getImageLoader());
        for (int i = 0; i <= results.get(position).getRating(); i++) {
            holder.stars.get(i).setImageDrawable(context.getResources().getDrawable(R.drawable.star));
        }
        holder.title.setText(results.get(position).getNameSurname());
        holder.updateDate.setText(AppController.getInstance().daysFromDate(results.get(position).getProfileUpdateDate()));
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static class SearchResultRecyclerViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView thumb;
        private ArrayList<ImageView> stars;
        private TextView title, updateDate;

        public SearchResultRecyclerViewHolder(View v) {
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

        public NetworkImageView getThumb() {
            return thumb;
        }

        public void setThumb(NetworkImageView thumb) {
            this.thumb = thumb;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(TextView updateDate) {
            this.updateDate = updateDate;
        }

        public ArrayList<ImageView> getStars() {
            return stars;
        }

        public void setStars(ArrayList<ImageView> stars) {
            this.stars = stars;
        }
    }
}
