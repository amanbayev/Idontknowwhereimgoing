package kz.growit.smartservice.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 03.07.2015.
 */
public class DetailProfile {
    private int Id, RegionId, CityId, ServiceCategoryId, RatingCount;
    private String NameSurname, Education, AboutMe, Experience, RegionName, CityName, Username, ProfileImageUrl, ServiceCategoryName, Latitude, Longitude;
    private Date ProfileUpdateDate;
    private ArrayList<String> AllPictures, AllBigPictures, AllSpecializations, AllLanguages;
    private ArrayList<UserReview> AllRatings;
    private double Rating;

    public DetailProfile(JSONObject jsonObject) {
        try {
            // ints
            this.Id = jsonObject.getInt("Id");
            this.RegionId = jsonObject.getInt("RegionId");
            this.CityId = jsonObject.getInt("CityId");
            this.ServiceCategoryId = jsonObject.getInt("ServiceCategoryId");
            this.RatingCount = jsonObject.getInt("RatingCount");

            // strings
            if (!jsonObject.getString("NameSurname").equals("null"))
                this.NameSurname = jsonObject.getString("NameSurname");
            if (!jsonObject.getString("Education").equals("null"))
                this.Education = jsonObject.getString("Education");
            if (!jsonObject.getString("AboutMe").equals("null"))
                this.AboutMe = jsonObject.getString("AboutMe");
            if (!jsonObject.getString("Experience").equals("null"))
                this.Experience = jsonObject.getString("Experience");
            if (!jsonObject.getString("RegionName").equals("null"))
                this.RegionName = jsonObject.getString("RegionName");
            if (!jsonObject.getString("CityName").equals("null"))
                this.CityName = jsonObject.getString("CityName");
            if (!jsonObject.getString("Username").equals("null"))
                this.Username = jsonObject.getString("Username");
            if (!jsonObject.getString("ProfileImageUrl").equals("null"))
                this.ProfileImageUrl = jsonObject.getString("ProfileImageUrl");
            if (!jsonObject.getString("ServiceCategoryName").equals("null"))
                this.ServiceCategoryName = jsonObject.getString("ServiceCategoryName");
            if (!jsonObject.getString("Latitude").equals("null"))
                this.Latitude = jsonObject.getString("Latitude");
            else
                this.Latitude = "51.1479457";
            if (!jsonObject.getString("Longitude").equals("null"))
                this.Longitude = jsonObject.getString("Longitude");
            else
                this.Longitude = "71.4793897";

            this.ProfileUpdateDate = AppController.getInstance().fromServerStringToDate(jsonObject.getString("ProfileUpdateDate"));

            JSONArray stringArray = jsonObject.getJSONArray("AllPictures");
            AllPictures = new ArrayList<>();
            for (int i = 0; i < stringArray.length(); i++)
                AllPictures.add(stringArray.getString(i));

            stringArray = jsonObject.getJSONArray("AllBigPictures");
            AllBigPictures = new ArrayList<>();
            for (int i = 0; i < stringArray.length(); i++)
                AllBigPictures.add(stringArray.getString(i));

            stringArray = jsonObject.getJSONArray("AllSpecializations");
            AllSpecializations = new ArrayList<>();
            for (int i = 0; i < stringArray.length(); i++)
                AllSpecializations.add(stringArray.getString(i));

            stringArray = jsonObject.getJSONArray("AllLanguages");
            AllLanguages = new ArrayList<>();
            for (int i = 0; i < stringArray.length(); i++)
                AllLanguages.add(stringArray.getString(i));

            stringArray = jsonObject.getJSONArray("AllRatings");
            AllRatings = new ArrayList<>();
            for (int i = 0; i < stringArray.length(); i++) {
                UserReview tUR = new UserReview(stringArray.getJSONObject(i));
                AllRatings.add(tUR);
            }

            Rating = jsonObject.getDouble("Rating");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return Id;
    }

    public int getRegionId() {
        return RegionId;
    }

    public int getCityId() {
        return CityId;
    }

    public int getServiceCategoryId() {
        return ServiceCategoryId;
    }

    public int getRatingCount() {
        return RatingCount;
    }

    public String getNameSurname() {
        return NameSurname;
    }

    public String getEducation() {
        return Education;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public String getExperience() {
        return Experience;
    }

    public String getRegionName() {
        return RegionName;
    }

    public String getCityName() {
        return CityName;
    }

    public String getUsername() {
        return Username;
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public String getServiceCategoryName() {
        return ServiceCategoryName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public Date getProfileUpdateDate() {
        return ProfileUpdateDate;
    }

    public ArrayList<String> getAllPictures() {
        return AllPictures;
    }

    public ArrayList<String> getAllBigPictures() {
        return AllBigPictures;
    }

    public ArrayList<String> getAllSpecializations() {
        return AllSpecializations;
    }

    public ArrayList<String> getAllLanguages() {
        return AllLanguages;
    }

    public ArrayList<UserReview> getAllRatings() {
        return AllRatings;
    }

    public double getRating() {
        return Rating;
    }
}
