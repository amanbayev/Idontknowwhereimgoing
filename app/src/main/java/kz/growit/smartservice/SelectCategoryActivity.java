package kz.growit.smartservice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.HashMap;

import kz.growit.smartservice.Models.ServiceCategory;

public class SelectCategoryActivity extends AppCompatActivity {
    private ArrayList<ImageView> catIcons = new ArrayList<>();
    private ArrayList<String> colorsForBadges = new ArrayList<>();
    private ArrayList<TextView> catTexts = new ArrayList<>();
    private Intent goToIntent;
    private ArrayList<android.support.v7.widget.CardView> badges = new ArrayList<>();
    private int[] indicies =
            {1, 6, 7, 19,
                    20, 21, 22, 23,
                    24, 25, 26, 27,
                    28, 29, 30};
    private HashMap<Integer, ServiceCategory> catMap = AppController.getInstance().getServiceCategories();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLargeButtons);
        toolbar.setTitle(getResources().getString(R.string.select_category_ru));

        initColors();
        initBadges();
        initIcons();
        initTexts();
        // set Icon colors to white
        for (int i = 0; i < catIcons.size(); i++) {
            catIcons.get(i).setColorFilter(Color.parseColor("#FFFFFF"));
        }
        // set texts to description values from server
        for (int i = 0; i < indicies.length; i++) {
            Integer tempKey = indicies[i];
            ServiceCategory temp = catMap.get(tempKey);
            catTexts.get(i).setText(temp.getDescription());
            catTexts.get(i).setTextColor(Color.WHITE);
            catTexts.get(i).setSelected(true);
        }
        // all categories badge special case
        catTexts.get(catTexts.size() - 1).setTextColor(Color.WHITE);
        catTexts.get(catTexts.size() - 1).setText(getResources().getString(R.string.all_categories_ru));
        // set card bg colors to color array values
        for (int i = 0; i < badges.size(); i++) {
            badges.get(i).setCardBackgroundColor(Color.parseColor(colorsForBadges.get(i)));
            if (i < 15) {
                badges.get(i).setTag(indicies[i]);
                badges.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(v);
                        goToIntent = new Intent(SelectCategoryActivity.this, SearchNewDesign.class);
                        goToIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        goToIntent.putExtra("catId", (Integer) v.getTag());
                        Handler myHandler = new Handler();
                        myHandler.postDelayed(mMyRunnable, 700);
                    }
                });
            } else {
                badges.get(i).setTag(0);
                badges.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .playOn(v);
                        Snackbar.make(v, "Данный функционал в разработке. Ждите в следующем обновлении.", Snackbar.LENGTH_LONG).show();
                    }
                });
            }

        }

    }

    private Runnable mMyRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(goToIntent);
        }
    };

    private void initColors() {
        colorsForBadges.add("#f44336");
        colorsForBadges.add("#E91E63");
        colorsForBadges.add("#9C27B0");
        colorsForBadges.add("#673AB7");

        colorsForBadges.add("#3F51B5");
        colorsForBadges.add("#2196F3");
        colorsForBadges.add("#03A9F4");
        colorsForBadges.add("#00BCD4");

        colorsForBadges.add("#009688");
        colorsForBadges.add("#4CAF50");
        colorsForBadges.add("#8BC34A");
        colorsForBadges.add("#CDDC39");

        colorsForBadges.add("#FFEB3B");
        colorsForBadges.add("#FFC107");
        colorsForBadges.add("#FF9800");
        colorsForBadges.add("#FF5722");
    }

    private void initBadges() {
        badges.add((CardView) findViewById(R.id.badgeCategory1));
        badges.add((CardView) findViewById(R.id.badgeCategory2));
        badges.add((CardView) findViewById(R.id.badgeCategory3));
        badges.add((CardView) findViewById(R.id.badgeCategory4));

        badges.add((CardView) findViewById(R.id.badgeCategory5));
        badges.add((CardView) findViewById(R.id.badgeCategory6));
        badges.add((CardView) findViewById(R.id.badgeCategory7));
        badges.add((CardView) findViewById(R.id.badgeCategory8));

        badges.add((CardView) findViewById(R.id.badgeCategory9));
        badges.add((CardView) findViewById(R.id.badgeCategory10));
        badges.add((CardView) findViewById(R.id.badgeCategory11));
        badges.add((CardView) findViewById(R.id.badgeCategory12));

        badges.add((CardView) findViewById(R.id.badgeCategory13));
        badges.add((CardView) findViewById(R.id.badgeCategory14));
        badges.add((CardView) findViewById(R.id.badgeCategory15));
        badges.add((CardView) findViewById(R.id.badgeCategory16));
    }

    private void initIcons() {
        catIcons.add((ImageView) findViewById(R.id.cat_one_image));
        catIcons.add((ImageView) findViewById(R.id.cat_six_image));
        catIcons.add((ImageView) findViewById(R.id.cat_seven_image));
        catIcons.add((ImageView) findViewById(R.id.cat_nineteen_image));

        catIcons.add((ImageView) findViewById(R.id.cat_20_image));
        catIcons.add((ImageView) findViewById(R.id.cat_21_image));
        catIcons.add((ImageView) findViewById(R.id.cat_22_image));
        catIcons.add((ImageView) findViewById(R.id.cat_23_image));

        catIcons.add((ImageView) findViewById(R.id.cat_24_image));
        catIcons.add((ImageView) findViewById(R.id.cat_25_image));
        catIcons.add((ImageView) findViewById(R.id.cat_26_image));
        catIcons.add((ImageView) findViewById(R.id.cat_27_image));

        catIcons.add((ImageView) findViewById(R.id.cat_28_image));
        catIcons.add((ImageView) findViewById(R.id.cat_29_image));
        catIcons.add((ImageView) findViewById(R.id.cat_30_image));
        catIcons.add((ImageView) findViewById(R.id.cat_all_image));
    }

    private void initTexts() {
        catTexts.add((TextView) findViewById(R.id.cat_one_text));
        catTexts.add((TextView) findViewById(R.id.cat_six_text));
        catTexts.add((TextView) findViewById(R.id.cat_seven_text));
        catTexts.add((TextView) findViewById(R.id.cat_nineteen_text));

        catTexts.add((TextView) findViewById(R.id.cat_20_text));
        catTexts.add((TextView) findViewById(R.id.cat_21_text));
        catTexts.add((TextView) findViewById(R.id.cat_22_text));
        catTexts.add((TextView) findViewById(R.id.cat_23_text));

        catTexts.add((TextView) findViewById(R.id.cat_24_text));
        catTexts.add((TextView) findViewById(R.id.cat_25_text));
        catTexts.add((TextView) findViewById(R.id.cat_26_text));
        catTexts.add((TextView) findViewById(R.id.cat_27_text));

        catTexts.add((TextView) findViewById(R.id.cat_28_text));
        catTexts.add((TextView) findViewById(R.id.cat_29_text));
        catTexts.add((TextView) findViewById(R.id.cat_30_text));
        catTexts.add((TextView) findViewById(R.id.cat_all_text));
    }


}
