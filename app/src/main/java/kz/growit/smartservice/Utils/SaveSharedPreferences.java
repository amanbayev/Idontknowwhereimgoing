package kz.growit.smartservice.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Талгат on 17.06.2015.
 */

public class SaveSharedPreferences {
    static final String PREF_USER_NAME = "login";
    static final String PREF_PASS_WORD = "password";
    static final String PREF_REGION_ID = "regionId";
    static final String PREF_CITY_ID = "cityId";
    static final String PREF_REGION_NAME = "regionName";
    static final String PREF_CITY_NAME = "cityName";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName, String passWord) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.putString(PREF_PASS_WORD, passWord);
        editor.commit();
    }

    public static void setCityId(Context ctx, String cityId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_CITY_ID, cityId);
        editor.commit();
    }

    public static void setCityName(Context ctx, String cityId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_CITY_NAME, cityId);
        editor.commit();
    }

    public static void setRegionName(Context ctx, String cityId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_REGION_NAME, cityId);
        editor.commit();
    }

    public static void setRegionId(Context ctx, String regionId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_REGION_ID, regionId);
        editor.commit();
    }

    public static String getCityId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_CITY_ID, "");
    }

    public static String getRegionId(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REGION_ID, "");
    }

    public static String getCityName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_CITY_NAME, "");
    }

    public static String getRegionName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REGION_NAME, "");
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static String getPassWord(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_PASS_WORD, "");
    }

    public static void clearUserName(Context ctx) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}



