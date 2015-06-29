package kz.growit.smartservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;

import kz.growit.smartservice.Adapters.MainMenuRecyclerAdapter;
import kz.growit.smartservice.Models.MainMenuCardItem;


public class MainMenu extends AppCompatActivity {

    private com.mikepenz.materialdrawer.Drawer drawer;
    private Toolbar toolbar;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbarMainMenu);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
        }

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarMainMenu);
        collapsingToolbar.setTitle("SmartService.KZ");

        drawer = AppController.getInstance().getDrawer(MainMenu.this, toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMainMenu);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<MainMenuCardItem> items = new ArrayList<>();

        items.add(new MainMenuCardItem(
                getResources().getString(R.string.search_intro_ru),
                getResources().getString(R.string.search_button_ru),1));
//        items.add(new MainMenuCardItem(
//                getResources().getString(R.string.offer_service_intro_ru),
//                getResources().getString(R.string.offer_service_button_ru),2));
        items.add(new MainMenuCardItem(
                getResources().getString(R.string.add_request_intro_ru),
                getResources().getString(R.string.add_request_buton_ru),3));

        MainMenuRecyclerAdapter myAdapter = new MainMenuRecyclerAdapter(items,MainMenu.this);

        recyclerView.setAdapter(myAdapter);

    }

}
