package com.dynepic.ppsdk_android;

import android.content.Context;
import android.util.Log;

import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._UserPrefs;

import java.util.HashMap;
import java.util.Map;

public class PPUserObject {
//	public String userId;
//	public String handle;
//	public String firstName;
//	public String lastName;
//	public String country;
//	public String accountType;
//	public String userType;
//	public Map<String, String> parentFlags;
//	public String profilePicId;
//	public String coverPhotoId;
//	public String myDataStorage;
//	public String myAppGlobalDataStorage;
//	public String myAge;
//	public boolean isAnonymousUser;
//	Map<String, String> myUserObjectMap;

	public User myUserObject;

//    public Map<String, String>getMyStoredProfile() {
//		Map<String, String> t = new HashMap<>();
//		t.putAll(myUserObjectMap);
//		return t;
//	}
//
//	public void populateUserData(Map<String, String> map) {
//		myUserObjectMap.putAll(map);
//	}
//
//	public void initWithUserPreferences(String id, String handle)
//	{
//		if(myUserObject == null) myUserObject = new User();
//		myUserObject.setUserId(id);
//		myUserObject.setHandle(handle);
//	}

	public void populateUserData(User user, Context CONTEXT) throws IllegalAccessException {
		Log.d("inflateWithUser:", user.toString());
		myUserObject = user;
		_UserPrefs userPrefs = new _UserPrefs(CONTEXT);
		userPrefs.setUserName(myUserObject.getValueForKey("firstName"));
		userPrefs.setUserDetails1(myUserObject.getValueForKey("lastName") + " is " + myUserObject.getValueForKey("handle"));
	}

	public String getValueForKey(String key) {
    	Log.d("getValueForKey:", "key: " + key);
		try {
			return myUserObject.getValueForKey(key);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

//	public void inflateFriendsListFromArray(ArrayList<User> users) throws IllegalAccessException {
//		Log.d("inflating friends list...", "");
//		for (User u: users) {
//			Log.d("friend:", u.getHandle());
//		}
//	}

}