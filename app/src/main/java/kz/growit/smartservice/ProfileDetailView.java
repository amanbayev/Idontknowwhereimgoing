package kz.growit.smartservice;

import kz.growit.smartservice.Models.DetailProfile;
import kz.growit.smartservice.Models.UserReview;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.daimajia.slider.library.SliderLayout;
import com.lid.lib.LabelView;
import com.mikepenz.materialdrawer.Drawer;
import com.rey.material.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileDetailView extends AppCompatActivity implements OnMapReadyCallback {
    private TextView nameSurname, servicesList, category, city, langaugesList, education, experience, about;
    private AppController myApp = AppController.getInstance();
    private ArrayList<ImageView> stars;
    private LinearLayout langaugesLL, educationLL, experienceLL, mapLL, servicesLL, sliderLL, showReviewsLL, showPicturesLL;
    private DetailProfile detailProfile;
    private LatLng loc;
    private Button showPhone, showPictures, showReviews;
    private boolean phoneIsShown = false;
    private SliderLayout slider;

    @Override
    public void onMapReady(GoogleMap myMap) {
        myMap.addMarker(new MarkerOptions()
                        .position(loc)
                        .title(detailProfile.getNameSurname())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_new))
        );
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc)
                .zoom(5)
                .bearing(45)
                .tilt(20)
                .build();
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        myMap.getUiSettings().setAllGesturesEnabled(false);
        myMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfileDetailView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawer drawer = AppController.getInstance().getDrawer(ProfileDetailView.this, toolbar);
        nameSurname = (TextView) findViewById(R.id.nameSurnameProfileDetailTextView);
        Typeface font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        nameSurname.setTypeface(font);
        CircularImageView thumb = (CircularImageView) findViewById(R.id.thumbProfileDetailView);
        thumb.setImageDrawable(myApp.getProfileDrawable());
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .playOn(v);
            }
        });
        servicesList = (TextView) findViewById(R.id.servicesListTextView);
        servicesList.setText("+ Catering \n+ Holidays \n+ Birthdays");
        category = (TextView) findViewById(R.id.categoryTextProfileDetailView);
        stars = new ArrayList<>();
        stars.add((ImageView) findViewById(R.id.starOneProfileDetailView));
        stars.add((ImageView) findViewById(R.id.starTwoProfileDetailView));
        stars.add((ImageView) findViewById(R.id.starThreeProfileDetailView));
        stars.add((ImageView) findViewById(R.id.starFourProfileDetailView));
        stars.add((ImageView) findViewById(R.id.starFiveProfileDetailView));
        city = (TextView) findViewById(R.id.cityProfileDetailView);
        slider = (SliderLayout) findViewById(R.id.sliderProfileDetail);
        sliderLL = (LinearLayout) findViewById(R.id.sliderLL);
        showPhone = (Button) findViewById(R.id.showPhoneProfileDetailViewButton);
        showPictures = (Button) findViewById(R.id.showPicturesProfileDetailViewButton);
        showReviews = (Button) findViewById(R.id.showReviewsProfileDetailViewButton);

        langaugesLL = (LinearLayout) findViewById(R.id.languagesLL);
        langaugesList = (TextView) findViewById(R.id.languagesListProfileDetailTextView);
        educationLL = (LinearLayout) findViewById(R.id.educationLL);
        education = (TextView) findViewById(R.id.educationTextProfileDetail);
        experienceLL = (LinearLayout) findViewById(R.id.experienceLL);
        experience = (TextView) findViewById(R.id.experienceTextProfileDetail);
        servicesLL = (LinearLayout) findViewById(R.id.servicesLL);
        showPicturesLL = (LinearLayout) findViewById(R.id.showPicturesLL);
        showReviewsLL = (LinearLayout) findViewById(R.id.showReviewsLL);
        mapLL = (LinearLayout) findViewById(R.id.mapLL);
        about = (TextView) findViewById(R.id.aboutProfileTextView);

        int id = myApp.getSelectedProfileId();
        String url = "http://smartservice.kz/api/UserProfilesApi/GetUserProfile/" + id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        detailProfile = new DetailProfile(response);
                        bindData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("load profile", error.getMessage().toString());
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myApp.addToRequestQueue(jsonObjectRequest, "load profile");

    }

    private void bindData() {
        nameSurname.setText(detailProfile.getNameSurname());
        nameSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.StandUp)
                        .duration(700)
                        .playOn(v);
            }
        });

        about.setText(detailProfile.getAboutMe());

        String servicesStr = "";
        for (int i = 0; i < detailProfile.getAllSpecializations().size(); i++) {
            servicesStr += "+ " + detailProfile.getAllSpecializations().get(i) + "\n";
        }
        if (servicesStr.equals(""))
            servicesLL.removeAllViews();
        else
            servicesList.setText(servicesStr);
        category.setText(detailProfile.getServiceCategoryName());
        city.setText(detailProfile.getCityName());
        for (int i = 0; i < detailProfile.getRating(); i++) {
            stars.get(i).setImageDrawable(getResources().getDrawable(R.drawable.star));
        }

        if (detailProfile.getEducation() != null)
            education.setText(detailProfile.getEducation());
        else educationLL.removeAllViews();

        if (detailProfile.getExperience() != null)
            experience.setText(detailProfile.getExperience());
        else experienceLL.removeAllViews();

        servicesStr = "";
        for (int i = 0; i < detailProfile.getAllLanguages().size(); i++) {
            servicesStr += "+ " + detailProfile.getAllLanguages().get(i) + "\n";
        }
        if (servicesStr.equals(""))
            langaugesLL.removeAllViews();
        else
            langaugesList.setText(servicesStr);
        loc = new LatLng(Double.parseDouble(detailProfile.getLatitude()), Double.parseDouble(detailProfile.getLongitude()));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapProfileDetailView);
        mapFragment.getMapAsync(this);
        if (detailProfile.getAllPictures().size() == 0) {
            sliderLL.removeAllViews();
            showPicturesLL.removeAllViews();
        } else {
            showPicturesLL.removeAllViews();
            for (int i = 0; i < detailProfile.getAllPictures().size(); i++) {
                DefaultSliderView defaultSliderView = new DefaultSliderView(this);
                defaultSliderView
                        .image("http://smartservice.kz/" + detailProfile.getAllPictures().get(i));
                slider.addSlider(defaultSliderView);
            }
            slider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
            slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
            //slider.setCustomAnimation(new DescriptionAnimation());
            slider.startAutoCycle();
            showPictures.setText(showPictures.getText() + " (" + detailProfile.getAllPictures().size() + ")");
            showPictures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to gallery
                }
            });

            // add Grow IT badge label (left-side)
            LabelView labelView = new LabelView(this);
            labelView.setText("Grow IT");
            labelView.setBackgroundColor(0xff03a9f4);
            labelView.setTargetView(findViewById(R.id.sliderLL), 10, LabelView.Gravity.LEFT_TOP);

            // add company badge label (right-side)
            LabelView label2 = new LabelView(this);
            label2.setText(detailProfile.getNameSurname());
            label2.setBackgroundColor(getResources().getColor(R.color.primary));
            int radii = detailProfile.getNameSurname().length();
            if (radii > 25) radii = 25;
            label2.setTargetView(findViewById(R.id.sliderLL), radii * 2, LabelView.Gravity.RIGHT_TOP);
        }
        showPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneIsShown) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + detailProfile.getUsername()));
                    startActivity(intent);
                } else {
                    phoneIsShown = true;
                    increasePhoneViewCount();
                    showPhone.setText("+" + detailProfile.getUsername());
                }
            }
        });
        if (detailProfile.getAllRatings().size() == 0) {
            showReviewsLL.removeAllViews();
        } else {
            showReviews.setText(showReviews.getText() + " (" + detailProfile.getAllRatings().size() + ")");
            myApp.setSelectedUserReviews(detailProfile.getAllRatings());
            showReviews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToRatings = new Intent(ProfileDetailView.this, ShowRatings.class);
                    goToRatings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(goToRatings);
                }
            });
        }
    }

    private void increasePhoneViewCount() {
        String url = "http://smartservice.kz/api/UserProfilesApi/IncreasePhoneViewCount";
        JSONObject data = new JSONObject();
        try {
            data.put("Username", detailProfile.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("phone view increase", "successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("phone view increase", error.getLocalizedMessage());
                    }
                }
        );
        myApp.addToRequestQueue(jsonObjectRequest, "increase phone count");
    }

    @Override
    protected void onStop() {
        if (detailProfile.getAllPictures().size() != 0) slider.stopAutoCycle();
        super.onStop();
    }
}
