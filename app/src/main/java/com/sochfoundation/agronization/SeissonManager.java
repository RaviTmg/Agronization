package com.sochfoundation.agronization;

import android.content.Context;
import android.content.SharedPreferences;

public class SeissonManager {
    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidLogin";

    private static final String USER_TOKEN = "userToken";

    public SeissonManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setUserToken(String token){
        editor.putString(USER_TOKEN, token);
        editor.commit();
    }
    public String getUserToken(){
        return pref.getString(USER_TOKEN, "");
    }
}
