package com.example.myapplication.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "yuda";
    private static final String KEY_NAME = "name";
    private static final String KEY_ID = "id";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_TIMER = "timer";

    private static final String Market_Table = "table";
    private static final String KEY_OPEN = "open";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setId(String id) {
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public String getId(){
        return pref.getString(KEY_ID, "0");
    }

    public void setName(String id) {
        editor.putString(KEY_NAME, id);
        editor.commit();
    }

    public String getName(){
        return pref.getString(KEY_NAME, "0");
    }

    public void setEmail(String id) {
        editor.putString(KEY_EMAIL, id);
        editor.commit();
    }

    public String getEmail(){
        return pref.getString(KEY_EMAIL, "0");
    }

    public void setUsername(String id) {
        editor.putString(KEY_USERNAME, id);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME, "0");
    }

    public void setMarket_Table(String id) {
        editor.putString(Market_Table, id);
        editor.commit();
    }

    public String getMarket_Table(){
        return pref.getString(Market_Table, "night");
    }

    public void setImage(String id) {
        editor.putString(KEY_IMAGE, id);
        editor.commit();
    }

    public String getImage(){
        return pref.getString(KEY_IMAGE, "no_pix.png");
    }

    public void setChat(boolean isLoggedIn) {
        editor.putBoolean(KEY_TIMER, isLoggedIn);
        editor.commit();
    }

    public boolean isChatIn(){
        return pref.getBoolean(KEY_TIMER, true);
    }

    public void setMarket_Open(String id) {
        editor.putString(KEY_OPEN, id);
        editor.commit();
    }

    public String getMarket_Open(){
        return pref.getString(KEY_OPEN, "all");
    }


}