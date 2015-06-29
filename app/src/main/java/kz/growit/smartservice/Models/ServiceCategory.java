package kz.growit.smartservice.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 25.06.2015.
 */
public class ServiceCategory {
    private int Id;
    private String Description;
    private HashMap<Integer, Specialization> specializations = new HashMap<>();

    public ServiceCategory() {
    }

    public ServiceCategory(JSONObject jsonObject) {
        try {
            this.Id = jsonObject.getInt("Id");
            this.Description = jsonObject.getString(AppController.getInstance().getLocalizedDecription());
            JSONArray mySpecializations = jsonObject.getJSONArray("SpecializationDtos");
            for (int i = 0; i < mySpecializations.length(); i++) {
                Specialization tempSpecialization = new Specialization(mySpecializations.getJSONObject(i));
                this.specializations.put(tempSpecialization.getId(), tempSpecialization);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public HashMap<Integer, Specialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(HashMap<Integer, Specialization> specializations) {
        this.specializations = specializations;
    }
}
