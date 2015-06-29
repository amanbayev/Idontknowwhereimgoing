package kz.growit.smartservice.Models;

import org.json.JSONException;
import org.json.JSONObject;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 25.06.2015.
 */
public class Specialization {
    private int Id;
    private String description;

    public Specialization(JSONObject jsonObject) {
        try {
            this.Id = jsonObject.getInt("Id");
            this.description = jsonObject.getString(AppController.getInstance().getLocalizedDecription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Specialization() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
