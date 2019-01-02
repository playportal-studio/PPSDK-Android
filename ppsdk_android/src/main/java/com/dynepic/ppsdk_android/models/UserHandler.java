package com.dynepic.ppsdk_android.models;

import android.content.Context;

import com.dynepic.ppsdk_android.utils._UserPrefs;

public class UserHandler {

	private static User userObject;
	private _UserPrefs userPrefs;
	private Context CONTEXT;

	public UserHandler(Context context) {
		CONTEXT = context;
		setUserPrefs(CONTEXT);
	}

	public void populateUserData(User userObject) {
		setUserPrefs(CONTEXT);
		setHandle(userObject.getHandle());
		setFirstName(userObject.getFirstName());
		setLastName(userObject.getLastName());
		setUserId(userObject.getUserId());
		setUserType(userObject.getUserType());
		setAccountType(userObject.getAccountType());
		setCoverPhoto(userObject.getCoverPhoto());
		setProfilePic(userObject.getProfilePic());
		setCountry(userObject.getCountry());
	}

	//region Getters and Setters
	private void setUserPrefs(Context context) {
		userPrefs = new _UserPrefs(context);
	}

	public String getAccountType() {
		return userPrefs.getAccountType();
	}

	public void setAccountType(String accountType) {
		userPrefs.setAccountType(accountType);
	}

	public String getCountry() {
		return userPrefs.getCountry();
	}

	public void setCountry(String country) {
		userPrefs.setCountry(country);
	}

	public String getCoverPhoto() {
		return userPrefs.getCoverPhoto();
	}

	public void setCoverPhoto(String coverPhoto) {
		userPrefs.setCoverPhoto(coverPhoto);
	}

	public String getFirstName() {
		return userPrefs.getFirstName();
	}

	public void setFirstName(String firstName) {
		userPrefs.setFirstName(firstName);
	}

	public String getHandle() {
		return userPrefs.getHandle();
	}

	public void setHandle(String handle) {
		userPrefs.setHandle(handle);
	}

	public String getLastName() {
		return userPrefs.getLastName();
	}

	public void setLastName(String lastName) {
		userPrefs.setLastName(lastName);
	}

	public String getProfilePic() {
		return userPrefs.getProfilePic();
	}

	public void setProfilePic(String profilePic) {
		userPrefs.setProfilePic(profilePic);
	}

	public String getUserId() {
		return userPrefs.getUserId();
	}

	public void setUserId(String userId) {
		userPrefs.setUserId(userId);
	}

	public String getUserType() {
		return userPrefs.getUserType();
	}

	public void setUserType(String userType) {
		userPrefs.setUserType(userType);
	}

	public String getMyDataStorage(String appName) {
		return userPrefs.myData(appName);
	}

	public String getMyGlobalDataStorage(String appName) { return userPrefs.globalData(appName); }



	//endregion

	public String toString(){
		setUserPrefs(CONTEXT);
		return "User:\n"+
				"Handle: "+getHandle()+"\n"+
				"First Name: "+getFirstName()+"\n"+
				"Last Name: "+getLastName()+"\n"+
				"User ID: "+getUserId()+"\n"+
				"User Type: "+getUserType()+"\n"+
				"Account Type: "+getAccountType()+"\n"+
				"Country: "+getCountry()+"\n";
	}


}