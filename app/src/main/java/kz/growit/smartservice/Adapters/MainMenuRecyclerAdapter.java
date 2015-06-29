package kz.growit.smartservice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kz.growit.smartservice.AddRequestActivity;
import kz.growit.smartservice.Models.MainMenuCardItem;
import kz.growit.smartservice.R;
import kz.growit.smartservice.Search;


/**
 * Created by Талгат on 28.06.2015.
 */
public class MainMenuRecyclerAdapter extends RecyclerView.Adapter<MainMenuRecyclerAdapter.ViewHolder> {
    private ArrayList<MainMenuCardItem> menuItems;
    private Context context;
    private int lastPosition = -1;

    public MainMenuRecyclerAdapter(ArrayList<MainMenuCardItem> listOfItems, Context context) {
        this.menuItems = listOfItems;
        this.context = context;
    }

    @Override
    public MainMenuRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.introTextView.setText(menuItems.get(position).getIntroText());
        holder.menuButton.setText(menuItems.get(position).getButtonText());
        holder.menuButton.setTag(menuItems.get(position).getTag());
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 1: // search
                        Intent goToSearch = new Intent(context, Search.class);
                        goToSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(goToSearch);
                        break;
                    case 2: // offer
                        Snackbar.make(v, "Ведется работа над функционалом", Snackbar.LENGTH_LONG).show();
                        break;
                    case 3: // add request
                        Intent goToAddRequest = new Intent(context, AddRequestActivity.class);
                        goToAddRequest.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(goToAddRequest);
                        break;
                    default:
                        Snackbar.make(v, "Добро пожаловать в SmartService!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView introTextView;
        protected Button menuButton;

        public ViewHolder(View itemView) {
            super(itemView);
            introTextView = (TextView) itemView.findViewById(R.id.mainMenuCardItemTextView);
            menuButton = (Button) itemView.findViewById(R.id.mainMenuCardItemButton);
        }
    }

    // Apply some custom animations to loading the items in the list
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
