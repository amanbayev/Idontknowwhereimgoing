package kz.growit.smartservice.Models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 29.06.2015.
 */
public class SearchResultItem {
    private String profileImageUrl, nameSurname;
    private Date profileUpdateDate;
    private int id, ratingCount, rating;
    private LatLng location;

    public SearchResultItem(JSONObject obj) {
        try {
            this.profileImageUrl = obj.getString("ProfileImageUrl");
            this.profileUpdateDate = AppController.getInstance().fromServerStringToDate(obj.getString("ProfileUpdateDate"));
            this.rating = obj.getInt("Rating");
            this.nameSurname = obj.getString("NameSurname");
            String lat = obj.getString("Latitude");
            if (lat.equals("null")) lat = "0";
            String lon = obj.getString("Longitude");
            if (lon.equals("null")) lon = "0";
            this.location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            this.ratingCount = obj.getInt("RatingCount");
            this.id = obj.getInt("Id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public Date getProfileUpdateDate() {
        return profileUpdateDate;
    }

    public void setProfileUpdateDate(Date profileUpdateDate) {
        this.profileUpdateDate = profileUpdateDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}


