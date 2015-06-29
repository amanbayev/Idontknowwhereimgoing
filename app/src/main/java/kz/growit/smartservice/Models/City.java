package kz.growit.smartservice.Models;

import org.json.JSONException;
import org.json.JSONObject;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 25.06.2015.
 */
public class City {
    private int id;
    private String description;

    public City() {
    }

    public City(JSONObject jsonObject) {
        try {
            id = jsonObject.getInt("Id");
            description = jsonObject.getString(AppController.getInstance().getLocalizedDecription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
