package com.dynepic.ppsdk_android;

import android.util.Log;

import com.dynepic.ppsdk_android.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PPUserObject {

/*
	public String userId;
	public String handle;
	public String firstName;
	public String lastName;
	public String country;
	public String accountType;
	public String userType;
	public Map<String, String> parentFlags;
	public String profilePicId;
// 	public Image profilePic;
	public String coverPhotoId;
//	public Image coverPhoto;
	public String myDataStorage;
	public String myAppGlobalDataStorage; // shared among all app users
	public String myAge;
	public boolean isAnonymousUser;
*/

	Map<String, String> myUserObjectMap;

	public User myUserObject;

    public Map<String, String>getMyStoredProfile()
	{
		Map<String, String> t = new HashMap<>();
		t.putAll(myUserObjectMap);
		return t;
	}

	public void inflateWith(Map<String, String> map)
	{
		myUserObjectMap.putAll(map);
	}

	public void initWithUserPreferences(String id, String handle)
	{
		if(myUserObject == null) myUserObject = new User();
		myUserObject.setUserId(id);
		myUserObject.setHandle(handle);
	}

	public void inflateWith(User user) throws IllegalAccessException {
		Log.d("inflateWithUser:", user.toString());
		myUserObject = user;
		Log.d("User:", myUserObject.getValueForKey("firstName") + " " + myUserObject.getValueForKey("lastName") + " just logged in as: " + myUserObject.getValueForKey("handle"));
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

	public void inflateFriendsListFromArray(ArrayList<User> users) throws IllegalAccessException {
		Log.d("inflating friends list...", "");
		for (User u: users) {
			Log.d("friend:", u.getHandle());
		}
	}

}