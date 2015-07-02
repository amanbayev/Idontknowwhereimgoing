package kz.growit.smartservice.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import kz.growit.smartservice.Fragments.MyMapFragment;
import kz.growit.smartservice.Fragments.SearchResultsRecyclerViewFragment;
import kz.growit.smartservice.PageFragment;
import kz.growit.smartservice.R;

/**
 * Created by Талгат on 29.06.2015.
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[];
    private Context context;

    public FragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = new String[]{
                context.getResources().getString(R.string.list_ru),
                context.getResources().getString(R.string.map_ru)};
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return SearchResultsRecyclerViewFragment.newInstance();
        else
            return PageFragment.newInstance(1);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
