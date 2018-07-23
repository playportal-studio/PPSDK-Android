package com.dynepic.ppsdk_android.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dynepic.ppsdk_android.models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class _UserPrefs {

    //Base SharedPreferences
    private SharedPreferences.Editor _prefsEditor;
    private SharedPreferences _sharedPrefs;

    //User _Base
    private String
            userName = "userName",
            accountType = "userName",
            country = "country",
            coverPhoto = "coverPhoto",
            firstName = "firstName",
            handle = "handle",
            lastName = "lastName",
            profilePic = "profilePic",
            userId = "userId",
            userType = "userType",
            myDataStorage = "myDataStorage",
            myGlobalDataStorage = "myGlobalDataStorage",
            friends = "friends";


    @SuppressLint("CommitPrefEdits") //IDE thinks it's not making a commit() or apply()...
    public _UserPrefs(Context context) {
        this._sharedPrefs = context.getSharedPreferences("ppsdk-user-preferences", Context.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }

    public String getUserName() {
        return _sharedPrefs.getString(userName, "");
    }
    public void setUserName(String value) {
        _prefsEditor.putString(userName, value);
        _prefsEditor.apply();
    }

    public String getAccountType() {
        return _sharedPrefs.getString(accountType, "");
    }
    public void setAccountType(String value) {
        _prefsEditor.putString(accountType, value);
        _prefsEditor.apply();
    }

    public String getCountry() {
        return _sharedPrefs.getString(country, "");
    }
    public void setCountry(String value) {
        _prefsEditor.putString(country, value);
        _prefsEditor.apply();
    }

    public String getCoverPhoto() {
        return _sharedPrefs.getString(coverPhoto, "");
    }
    public void setCoverPhoto(String value) {
        _prefsEditor.putString(coverPhoto, value);
        _prefsEditor.apply();
    }

    public String getFirstName() {
        return _sharedPrefs.getString(firstName, "");
    }
    public void setFirstName(String value) {
        _prefsEditor.putString(firstName, value);
        _prefsEditor.apply();
    }

    public String getHandle() {
        return _sharedPrefs.getString(handle, "");
    }
    public void setHandle(String value) {
        _prefsEditor.putString(handle, value);
        _prefsEditor.apply();
    }

    public String getLastName() {
        return _sharedPrefs.getString(lastName, "");
    }
    public void setLastName(String value) {
        _prefsEditor.putString(lastName, value);
        _prefsEditor.apply();
    }

    public String getProfilePic() {
        return _sharedPrefs.getString(profilePic, "");
    }
    public void setProfilePic(String value) {
        _prefsEditor.putString(profilePic, value);
        _prefsEditor.apply();
    }

    public String getUserId() {
        return _sharedPrefs.getString(userId, "");
    }
    public void setUserId(String value) {
        _prefsEditor.putString(userId, value);
        _prefsEditor.apply();
    }

    public String getUserType() {
        return _sharedPrefs.getString(userType, "");
    }
    public void setUserType(String value) {
        _prefsEditor.putString(userType, value);
        _prefsEditor.apply();
    }

    public String getMyDataStorage(String appName) {
        return (handle + "@" + appName);
    }
    public String getMyGlobalDataStorage(String appName) {
		return("globalAppData" + "@" + appName);
    }

    public ArrayList<String> getFriendData(){
        Set<String> set = _sharedPrefs.getStringSet(friends, null);
        if (set!=null){//prevents it from blowing up
            return new ArrayList<>(set);
        } else{
            return null;
        }
    }
    public void setFriendData(ArrayList<String> FriendData){
        Set<String> set = new HashSet<String>();
        set.addAll(FriendData);
        _prefsEditor.putStringSet(friends, set);
        _prefsEditor.apply();
    }

    public boolean exists(){
        String USER = _sharedPrefs.getString(handle, "");
        String DETAILS = _sharedPrefs.getString(userId, "");
        return !USER.isEmpty() || !USER.equalsIgnoreCase("") || !DETAILS.isEmpty() || !DETAILS.equalsIgnoreCase("");
    }

    public void clear(){
        _prefsEditor.clear().commit();
    }

}
