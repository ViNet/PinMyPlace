package com.example.vit.pinmyplace.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.vit.pinmyplace.models.User;
import com.google.gson.Gson;


public class PrefUtils {

    private static String LOGIN_PREFS = "login_prefs";
    private static String KEY_USER = "user";

    public static void setCurrentUser(User currentUser, Context ctx) {
        SharedPreferences shp = ctx.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        Gson gson = new Gson();
        editor.putString(KEY_USER, gson.toJson(currentUser));
        editor.apply();
    }

    public static User getCurrentUser(Context ctx) {
        SharedPreferences shp = ctx.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        User user = null;

        if(shp.contains(KEY_USER)){
            String json = shp.getString(KEY_USER, "");
            Gson gson = new Gson();
            user = gson.fromJson(json, User.class);
        }
        return user;
    }

    public static void clearCurrentUser(Context ctx) {
        SharedPreferences shp = ctx.getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.clear();
        editor.apply();
    }
}
