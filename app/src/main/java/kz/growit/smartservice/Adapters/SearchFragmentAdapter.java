package kz.growit.smartservice.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kz.growit.smartservice.AppController;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 29.06.2015.
 */
public class SearchFragmentAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
    protected ArrayList<SearchResultItem> results;
    protected Context context;

    public SearchFragmentAdapter() {
        results = AppController.getInstance().getRecentSearch();
    }

    public SearchFragmentAdapter getInstance(Context context) {
        SearchFragmentAdapter sfa = new SearchFragmentAdapter();
        sfa.context = context;
        return sfa;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_recycler_view_item_row, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        SearchResultItem item = results.get(position);
        holder.thumb.setImageUrl(item.getProfileImageUrl(), AppController.getInstance().getImageLoader());
        for (int i = 0; i < item.getRating(); i++) {
            holder.stars.get(i).setImageDrawable(context.getResources().getDrawable(R.drawable.star));
        }
        holder.title.setText(item.getNameSurname());
        holder.updateDate.setText(AppController.getInstance().daysFromDate(item.getProfileUpdateDate()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void addOrUpdateSearchResult(SearchResultItem sri) {
        int pos = results.indexOf(sri);
        if (pos >= 0) {
            updateSearchResult(sri, pos);
        } else {
            addSearchResult(sri);
        }
    }

    private void updateSearchResult(SearchResultItem item, int pos) {
        results.remove(pos);
        notifyItemRemoved(pos);
        addSearchResult(item);
    }

    private void addSearchResult(SearchResultItem item) {
        results.add(item);
        notifyItemInserted(results.size() - 1);
    }
}
