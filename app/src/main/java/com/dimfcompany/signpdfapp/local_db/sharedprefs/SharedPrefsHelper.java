package com.dimfcompany.signpdfapp.local_db.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsHelper
{
    private final Context context;
    private final Gson gson;

    public SharedPrefsHelper(Context context, Gson gson)
    {
        this.context = context;
        this.gson = gson;
    }

    public void saveUserToken(String token)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_SHARED_PREFS, MODE_PRIVATE).edit();
        editor.putString(Constants.FB_TOKEN, token);
        editor.commit();
    }

    @Nullable
    public String getUserToken()
    {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MY_SHARED_PREFS, MODE_PRIVATE);
        String token = prefs.getString(Constants.FB_TOKEN, null);
        return token;
    }

    public void saveUserToShared(Model_User user)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_SHARED_PREFS, MODE_PRIVATE).edit();
        String json = gson.toJson(user);
        editor.putString(Constants.CURRENT_USER, json);
        editor.commit();
    }


    @Nullable
    public Model_User getUserFromSharedPrefs()
    {
        SharedPreferences prefs = context.getSharedPreferences(Constants.MY_SHARED_PREFS, MODE_PRIVATE);
        String json = prefs.getString(Constants.CURRENT_USER, null);
        Model_User user = gson.fromJson(json, Model_User.class);
        return user;
    }

    public void clearUserLocalData()
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.MY_SHARED_PREFS, MODE_PRIVATE).edit();
        editor.remove(Constants.CURRENT_USER);
        editor.apply();
    }
}
