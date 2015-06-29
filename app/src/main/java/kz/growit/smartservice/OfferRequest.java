package kz.growit.smartservice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;


public class OfferRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarOfferRequest);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawer drawer = AppController.getInstance().getDrawer(OfferRequest.this, toolbar);

    }

}
