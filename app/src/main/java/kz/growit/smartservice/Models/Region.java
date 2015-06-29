package kz.growit.smartservice.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 25.06.2015.
 */
public class Region {
    private HashMap<Integer, City> cities = new HashMap<>();
    private int id;
    private String description;

    public Region() {
    }

    public Region(JSONObject jsonObject) {
        try {
            this.id = jsonObject.getInt("Id");
            this.description = jsonObject.getString(AppController.getInstance().getLocalizedDecription());
            JSONArray myCities = jsonObject.getJSONArray("Cities");
            for (int i = 0; i < myCities.length(); i++) {
                City tempCity = new City(myCities.getJSONObject(i));
                this.cities.put(tempCity.getId(), tempCity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Integer, City> getCities() {
        return cities;
    }

    public void setCities(HashMap<Integer, City> cities) {
        this.cities = cities;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
