package com.dynepic.ppsdk_android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class _UserPrefs {

    //Base SharedPreferences
    private SharedPreferences.Editor _prefsEditor;
    private SharedPreferences _sharedPrefs;

    //User _Base
    private String USER_NAME = "USER_NAME";
    private String USER_DETAILS_1 = "USER_DETAILS_1";
    private String USER_DETAILS_2 = "USER_DETAILS_2";
    private String USER_DETAILS_3 = "USER_DETAILS_3";


    @SuppressLint("CommitPrefEdits") //IDE thinks it's not making a commit() or apply()...
    public _UserPrefs(Context context) {
        this._sharedPrefs = context.getSharedPreferences("ppsdk-user-preferences", Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getUserName() {
        return _sharedPrefs.getString(USER_NAME, "");
    }
    public void setUserName(String value) {
        _prefsEditor.putString(USER_NAME, value);
        _prefsEditor.apply();
    }


    public String getUserDetails1() {
        return _sharedPrefs.getString(USER_DETAILS_1, "");
    }
    public void setUserDetails1(String value) {
        _prefsEditor.putString(USER_DETAILS_1, value);
        _prefsEditor.apply();
    }


    public boolean exists(){
        String USER = _sharedPrefs.getString(USER_NAME, "");
        String DETAILS = _sharedPrefs.getString(USER_DETAILS_1, "");
        return !USER.isEmpty() || !USER.equalsIgnoreCase("") || !DETAILS.isEmpty() || !DETAILS.equalsIgnoreCase("");
    }

    public void clear(){
        _prefsEditor.clear().commit();
    }

}
