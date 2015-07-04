package kz.growit.smartservice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import kz.growit.smartservice.AppController;
import kz.growit.smartservice.Models.UserReview;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 04.07.2015.
 */
public class UserReviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    private ArrayList<UserReview> reviews = AppController.getInstance().getSelectedUserReviews();

    public UserReviewAdapter(Activity activity, ArrayList<UserReview> reviews) {
        this.activity = activity;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.user_review_row, null);
        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        TextView ratingUpdate = (TextView) convertView.findViewById(R.id.ratingUpdateTime);
        TextView ratingText = (TextView) convertView.findViewById(R.id.ratingDescriptionTextView);

        ratingBar.setRating(reviews.get(position).getFeedback());
        ratingUpdate.setText(AppController.getInstance().daysFromDate(reviews.get(position).getCreateDateTime()));
        ratingText.setText(reviews.get(position).getFeedbackText());

        return convertView;
    }
}
