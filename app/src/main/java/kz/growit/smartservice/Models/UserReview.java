package kz.growit.smartservice.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import kz.growit.smartservice.AppController;

/**
 * Created by Талгат on 03.07.2015.
 */
public class UserReview {
    private int Feedback;
    private String FeedbackText;
    private Date CreateDateTime;

    public UserReview(JSONObject jsonObject) {
        try {
            this.Feedback = jsonObject.getInt("Feedback");
            this.FeedbackText = jsonObject.getString("FeedbackText");
            this.CreateDateTime = AppController.getInstance().fromServerStringToDate(jsonObject.getString("CreateDateTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getFeedback() {
        return Feedback;
    }

    public String getFeedbackText() {
        return FeedbackText;
    }

    public Date getCreateDateTime() {
        return CreateDateTime;
    }
}
