package kz.growit.smartservice.Fragments;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import kz.growit.smartservice.Adapters.SearchResultRecyclerViewItemAdapter;
import kz.growit.smartservice.AppController;
import kz.growit.smartservice.R;

public class SearchResultsRecyclerViewFragment extends android.support.v4.app.Fragment {

    private android.support.v7.widget.RecyclerView recyclerView;

    public static SearchResultsRecyclerViewFragment newInstance() {
        SearchResultsRecyclerViewFragment fragment = new SearchResultsRecyclerViewFragment();
        return fragment;
    }

    public SearchResultsRecyclerViewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_results_recycler_view_fragment, container, false);

        //Get Handle to your UI elements
        initUi(view);
        return view;
    }

    private void initUi(View view) {
        //Get the handle to the recycler view
        recyclerView = (android.support.v7.widget.RecyclerView) view.findViewById(R.id.recyclerViewSearchFragmentTwo);
        configureRecyclerView();
        //Initiate your other elements here
        // ...
    }

    private void configureRecyclerView() {

        // Setup layout manager for items
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Control orientation of the items
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);

        // Attach layout manager to the RecyclerView
        recyclerView.setLayoutManager(layoutManager);

        // allows for optimizations if all item views are of the same size:
        recyclerView.setHasFixedSize(true);

        // Reference : https://gist.githubusercontent.com/alexfu/0f464fc3742f134ccd1e/raw/abe729359e5b3691f2fe56445644baf0e40b35ba/DividerItemDecoration.java
//        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
//        recyclerView.addItemDecoration(itemDecoration);

        // RecyclerView uses this by default. You can add custom animations by using RecyclerView.ItemAnimator()
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Handle item touch events
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }

            @Override
            public void onTouchEvent(RecyclerView recycler, MotionEvent event) {
                //Handle on touch events
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recycler, MotionEvent event) {
                return false;
            }
        });

        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        // Bind adapter to recycler view object
        recyclerView.setAdapter(new SearchResultRecyclerViewItemAdapter(getActivity().getApplicationContext(), AppController.getInstance().getRecentSearch()));
    }
}
