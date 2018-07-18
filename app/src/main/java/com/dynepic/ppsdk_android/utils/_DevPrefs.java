package com.dynepic.ppsdk_android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class _DevPrefs {

    private SharedPreferences.Editor _prefsEditor;
    private SharedPreferences _sharedPrefs;

    private String CLIENT_ID = "CLIENT_ID";
    private String CLIENT_SEC = "CLIENT_SEC";
    private String CLIENT_REDIRECT = "CLIENT_REDIRECT";
    private String CLIENT_ACCESS = "CLIENT_ACCESS";


    @SuppressLint("CommitPrefEdits")
    public _DevPrefs(Context context) {
        this._sharedPrefs = context.getSharedPreferences("ppsdk-dev-preferences", Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getClientId() {
        return _sharedPrefs.getString(CLIENT_ID, "");
    }

    public void setClientId(String value) {
        _prefsEditor.putString(CLIENT_ID, value);
        _prefsEditor.apply();
    }

    public String getClientSecret() {
        return _sharedPrefs.getString(CLIENT_SEC, "");
    }

    public void setClientSecret(String value) {
        _prefsEditor.putString(CLIENT_SEC, value);
        _prefsEditor.apply();
    }

    public String getClientRedirect() {
        return _sharedPrefs.getString(CLIENT_REDIRECT, "");
    }

    public void setClientRedirect(String value) {
        _prefsEditor.putString(CLIENT_REDIRECT, value);
        _prefsEditor.apply();
    }

    public String getClientAccessToken() {
        return _sharedPrefs.getString(CLIENT_ACCESS, "");
    }

    public void setClientAccessToken(String value) {
        _prefsEditor.putString(CLIENT_ACCESS, value);
        _prefsEditor.apply();
    }

    public boolean exists(){
        String USER = _sharedPrefs.getString(CLIENT_ID, "");
        String KEY = _sharedPrefs.getString(CLIENT_SEC, "");
        String RED = _sharedPrefs.getString(CLIENT_REDIRECT, "");
        return !USER.isEmpty() || !USER.equalsIgnoreCase("") ||
               !KEY.isEmpty() || !KEY.equalsIgnoreCase("") ||
               !RED.isEmpty() || !RED.equalsIgnoreCase("");
    }

    public void clear(){
        _prefsEditor.clear().commit();
    }

}
