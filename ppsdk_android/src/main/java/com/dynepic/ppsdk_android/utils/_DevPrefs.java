package com.dynepic.ppsdk_android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.ZonedDateTime;

public class _DevPrefs {

    private SharedPreferences.Editor _prefsEditor;
    private SharedPreferences _sharedPrefs;

    private String CLIENT_ID = "CLIENT_ID";
    private String CLIENT_SEC = "CLIENT_SEC";
    private String CLIENT_REDIRECT = "CLIENT_REDIRECT";
    private String ENVIRONMENT = "ENVIRONMENT";
    private String CLIENT_ACCESS = "CLIENT_ACCESS";
    private String CLIENT_REFRESH = "CLIENT_REFRESH";
    private String TOKEN_EXPIRATION_TIME = "TOKEN_EXPIRATION_TIME";
    private String APP_NAME = "APP_NAME";
    private String DEVICE_TOKEN = "DEVICE_TOKEN";

    private String cachedAccessToken = "";

    @SuppressLint("CommitPrefEdits")
    public _DevPrefs(Context context) {
        this._sharedPrefs = context.getSharedPreferences("ppsdk-dev-preferences", Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getDeviceToken() {
        return _sharedPrefs.getString(DEVICE_TOKEN, "");
    }
    public void setDeviceToken(String value) {
        _prefsEditor.putString(DEVICE_TOKEN, value);
        _prefsEditor.apply();
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

    public String getBaseUrl() {
        return _sharedPrefs.getString(ENVIRONMENT, "https://sandbox.iokids.net");
    }
    public void setBaseUrl(String value) {
        _prefsEditor.putString(ENVIRONMENT, value);
        _prefsEditor.apply();
    }

    public String getClientAccessToken() {
        return _sharedPrefs.getString(CLIENT_ACCESS, "");
    }
    public void setClientAccessToken(String value) {
        cachedAccessToken = value;
        _prefsEditor.putString(CLIENT_ACCESS, value);
        _prefsEditor.apply();
    }

    public String getClientRefreshToken() {
        return _sharedPrefs.getString(CLIENT_REFRESH, "");
    }
    public void setClientRefreshToken(String value) {
        _prefsEditor.putString(CLIENT_REFRESH, value);
        _prefsEditor.apply();
    }

    public String getTokenExpirationTime() {
        return _sharedPrefs.getString(TOKEN_EXPIRATION_TIME, "");
    }
    public void setTokenExpirationTime(String value) {
        _prefsEditor.putString(TOKEN_EXPIRATION_TIME, value);
        _prefsEditor.apply();
    }

    public String getAppName() { return _sharedPrefs.getString(APP_NAME, "unknown"); }
        public void setAppName(String value) {
        _prefsEditor.putString(APP_NAME, value);
        _prefsEditor.apply();
    }

    public void setAuthParms(String at, String rt, String t) {
        setClientAccessToken(at);
        setClientRefreshToken(rt);
        setTokenExpirationTime(t);
    }

    public boolean exists(){
        String USER = _sharedPrefs.getString(CLIENT_ID, "");
        String KEY = _sharedPrefs.getString(CLIENT_SEC, "");
        String RED = _sharedPrefs.getString(CLIENT_REDIRECT, "");
        String AT = _sharedPrefs.getString(CLIENT_ACCESS, "");
        String RT = _sharedPrefs.getString(CLIENT_REFRESH, "");
        String EXPT = _sharedPrefs.getString(TOKEN_EXPIRATION_TIME, "");

        ZonedDateTime date = ZonedDateTime.now();
        Boolean expired =  _Utils.dateTimeFromString(EXPT).isAfter(date);
        Log.d("USER:", USER + " KEY:" + KEY + " RED:" + RED + " AT:" + AT + " RT:" + RT + " expired:" + expired);
        return !(USER.isEmpty() || KEY.isEmpty() || RED.isEmpty() ||  RT.isEmpty());  // Ok to have invalid or expired AccessToken (will be fixed by refresh)
    }

    public void clear(){
        setClientAccessToken("");
        setClientRefreshToken("");
        setBaseUrl("https://unknown");
        setClientId("");
        setClientSecret("");
    }

}
