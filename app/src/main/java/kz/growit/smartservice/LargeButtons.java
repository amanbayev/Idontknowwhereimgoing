package kz.growit.smartservice;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;


public class LargeButtons extends AppCompatActivity {

    private Drawer drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_buttons);
        toolbar = (Toolbar) findViewById(R.id.toolbarLargeButtons);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SmartService.kz");
        drawer = AppController.getInstance().getDrawer(LargeButtons.this, toolbar);

        ImageView write = (ImageView) findViewById(R.id.writeIcon);
        ImageView add = (ImageView) findViewById(R.id.addIcon);
        ImageView search = (ImageView) findViewById(R.id.searchIcon);
        ImageView profile = (ImageView) findViewById(R.id.profilIcon);

        write.setColorFilter(Color.parseColor("#ffffff"));
        add.setColorFilter(Color.parseColor("#ffffff"));
        search.setColorFilter(Color.parseColor("#ffffff"));
        profile.setColorFilter(Color.parseColor("#ffffff"));

        LinearLayout addLL, offerLL, searchLL, profileLL;
        addLL = (LinearLayout) findViewById(R.id.addRequestLargeButtonLL);
        offerLL = (LinearLayout) findViewById(R.id.offerServiceLargeButtonLL);
        searchLL = (LinearLayout) findViewById(R.id.searchLargeButtonLL);
        profileLL = (LinearLayout) findViewById(R.id.myProfileLargeButtonLL);

        final TextView footer = (TextView) findViewById(R.id.footerText);

        addLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footer.setText("Add clicked");
            }
        });

        offerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footer.setText("offer clicked");
            }
        });

        searchLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToNewDesignSearch = new Intent(LargeButtons.this, SearchNewDesign.class);
                goToNewDesignSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goToNewDesignSearch);
            }
        });

        profileLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                footer.setText("Profile clicked");
            }
        });
    }


}
