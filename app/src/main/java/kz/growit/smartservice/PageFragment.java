package kz.growit.smartservice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kz.growit.smartservice.Adapters.SearchFragmentAdapter;


/**
 * Created by Талгат on 29.06.2015.
 */
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public SearchFragmentAdapter sra;
    private android.support.v7.widget.RecyclerView recyclerView;
    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_results_recycler_view_fragment, container, false);
        recyclerView = (android.support.v7.widget.RecyclerView) view.findViewById(R.id.recyclerViewSearchFragmentTwo);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        sra = new SearchFragmentAdapter().getInstance(getActivity().getApplicationContext());
        recyclerView.setAdapter(sra);
        return view;
    }
}
