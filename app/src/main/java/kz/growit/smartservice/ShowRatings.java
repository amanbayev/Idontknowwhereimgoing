package kz.growit.smartservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import kz.growit.smartservice.Adapters.UserReviewAdapter;


public class ShowRatings extends AppCompatActivity {

    private ListView ratings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ratings);
        ratings = (ListView) findViewById(R.id.ratingsListView);
        UserReviewAdapter myAdapter = new UserReviewAdapter(ShowRatings.this, AppController.getInstance().getSelectedUserReviews());
        ratings.setAdapter(myAdapter);
    }
}
