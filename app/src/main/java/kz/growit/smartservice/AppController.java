package kz.growit.smartservice;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.lid.lib.LabelView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kz.growit.smartservice.Adapters.SearchFragmentAdapter;
import kz.growit.smartservice.Models.City;
import kz.growit.smartservice.Models.Region;
import kz.growit.smartservice.Models.SearchResultItem;
import kz.growit.smartservice.Models.ServiceCategory;
import kz.growit.smartservice.Models.Specialization;
import kz.growit.smartservice.Utils.LruBitmapCache;

/**
 * Created by Талгат on 25.06.2015.
 */
public class AppController extends Application {
    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private String language = "ru";
    private ArrayList<SearchResultItem> recentSearch;
    private Boolean isLoggedIn;
    private JSONObject token;
    private SearchFragmentAdapter sfa;
    private ImageLoader mImageLoader;
    private String regid;
    public static final String TAG = AppController.class.getSimpleName();
    private HashMap<Integer, Region> regions;
    private HashMap<Integer, ServiceCategory> categories;
    private int selectedRegionId = 1, selectedCityId = 1, selectedCategoryId = 1, selectedSpecializationId = 14;

    ////////////////////////////////////////////////
    public String getCity(int regionId, int cityId) {
        ArrayList<City> temp = getCitiesForRegion(regionId);
        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).getId() == cityId)
                return temp.get(i).getDescription();
        }
        return "";
    }

    public int getSelectedSpecializationId() {
        return selectedSpecializationId;
    }

    public void setSelectedSpecializationId(int selectedSpecializationId) {
        this.selectedSpecializationId = selectedSpecializationId;
    }

    public int getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(int selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public int getSelectedRegionId() {
        return selectedRegionId;
    }

    public void setSelectedRegionId(int selectedRegionId) {
        this.selectedRegionId = selectedRegionId;
    }

    public int getSelectedCityId() {
        return selectedCityId;
    }

    public void setSelectedCityId(int selectedCityId) {
        this.selectedCityId = selectedCityId;
    }

    public SearchFragmentAdapter getSfa() {
        return sfa;
    }

    public void setSfa(SearchFragmentAdapter sfa) {
        this.sfa = sfa;
    }

    public ArrayList<Region> getRegionsArray() {
        ArrayList<Region> response = new ArrayList<>();
        if (regions != null) {
            for (Region value : regions.values()) {
                response.add(value);
            }
        }
        return response;
    }

    public ArrayList<SearchResultItem> getRecentSearch() {
        return recentSearch;
    }

    public void setRecentSearch(ArrayList<SearchResultItem> recentSearch) {
        this.recentSearch = recentSearch;
    }

    public ArrayList<ServiceCategory> getCategoriesArray() {
        ArrayList<ServiceCategory> response = new ArrayList<>();
        if (categories != null) {
            for (ServiceCategory value : categories.values()) {
                response.add(value);
            }
        }
        return response;
    }

    public ArrayList<City> getCitiesForRegion(int id) {
        Region cur = regions.get(id);
        ArrayList<City> response = new ArrayList<>();
        for (City value : cur.getCities().values()) {
            response.add(value);
        }
        return response;
    }

    public ArrayList<Specialization> getSpecializationsForServiceCategory(int id) {
        ServiceCategory cur = categories.get(id);
        ArrayList<Specialization> response = new ArrayList<>();
        for (Specialization value : cur.getSpecializations().values()) {
            response.add(value);
        }
        return response;
    }

    public JSONObject getToken() {
        return token;
    }

    public void setToken(JSONObject token) {
        this.token = token;
    }

    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Drawer getDrawer(final Activity activity, Toolbar toolbar) {
        Drawer drawer = new DrawerBuilder()
                .withActivity(activity)
                .withTranslucentStatusBar(true)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withTranslucentActionBarCompatibility(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        // 0 home
                        new PrimaryDrawerItem().withIdentifier(0).withName(getResources().getString(R.string.drawer_item_main_ru)).withIcon(getResources().getDrawable(R.drawable.home)),
                        // 1 offerRequest
                        //new PrimaryDrawerItem().withIdentifier(1).withName(getResources().getString(R.string.drawer_item_offer_request_ru)).withIcon(getResources().getDrawable(R.drawable.plus_circle_outline)),
                        // 2 findRequest
                        new PrimaryDrawerItem().withIdentifier(2).withName(getResources().getString(R.string.drawer_item_find_request_ru)).withIcon(getResources().getDrawable(R.drawable.magnify)),
                        // 3 addRequest
                        new PrimaryDrawerItem().withIdentifier(3).withName(getResources().getString(R.string.drawer_item_add_request_ru)).withIcon(getResources().getDrawable(R.drawable.pen))
                        //,

                        //     new SectionDrawerItem().withName(getResources().getString(R.string.section_my_profile_ru)),
                        // 4 myRequests
                        //       new PrimaryDrawerItem().withIdentifier(4).withName(getResources().getString(R.string.drawer_item_my_requests_ru)).withIcon(getResources().getDrawable(R.drawable.file_document))
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        int id = iDrawerItem.getIdentifier();
                        switch (id) {
                            case 0:
                                Intent goToMain = new Intent(activity, MainMenu.class);
                                goToMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(goToMain);
                                return true;
                            case 1:
                                Intent goToOfferRequest = new Intent(activity, Login.class);
                                goToOfferRequest.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(goToOfferRequest);
                                return true;
                            case 2:
                                Intent goToSearch = new Intent(activity, Search.class);
                                goToSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(goToSearch);
                            case 3:
                                Intent goToAddRequest = new Intent(activity, AddRequestActivity.class);
                                goToAddRequest.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(goToAddRequest);
                            default:
                                return true;
                        }
                    }
                })
                .build();

        LabelView label = new LabelView(activity);
        label.setText("SmartService.kz");
        label.setTextSize(8);
        label.setBackgroundColor(activity.getResources().getColor(R.color.primary));
        label.setTargetView(activity.findViewById(R.id.headerBackground), 15, LabelView.Gravity.LEFT_TOP);

        return drawer;
    }

    public HashMap<Integer, ServiceCategory> getServiceCategories() {
        return categories;
    }

    public void setServiceCategories(HashMap<Integer, ServiceCategory> serviceCategories) {
        this.categories = serviceCategories;
    }

    public HashMap<Integer, Region> getRegions() {
        return regions;
    }

    public void setRegions(HashMap<Integer, Region> regions) {
        this.regions = regions;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public String daysFromDate(Date date) {
        Date today = new Date();
        String response;

        int between = (int) ((int) today.getTime() - date.getTime());
        between = between / (1000 * 60);
        response = "мин. назад";
        if (between > 59) {
            between = between / 60;
            response = "час. назад";
            if (between > 24) {
                between = between / 24;
                response = "дн. назад";
            }
        }

        return between + " " + response;
    }

    public String getLocalizedDecription() {
        switch (this.language) {
            case "ru":
                return "Description";
            case "en":
                return "DescriptionEn";
            case "kz":
                return "DescriptionKz";
            default:
                return "Description";
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public String jsonToUrlEncodedString(JSONObject jso) {
        String ok = "";
        JSONArray names = jso.names();
        for (int i = 0; i < jso.length(); i++) {
            try {
                String key = names.getString(i);
                String value = jso.getString(key);
                if (ok.length() > 0) ok = ok + "&";
                ok = ok + key + "=" + value;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ok;
    }

    public String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return simpleDateFormat.format(date);
    }

    public Date fromServerStringToDate(String serverDate) {
        //"ProfileUpdateDate": "2015-06-04T20:38:25.967"
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            return df.parse(serverDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
