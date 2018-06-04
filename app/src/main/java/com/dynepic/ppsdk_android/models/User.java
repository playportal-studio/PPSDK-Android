package com.dynepic.ppsdk_android.models;

import android.util.Log;

import com.dynepic.ppsdk_android.PPManager;
import com.dynepic.ppsdk_android.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.Map;

public class User {

	@SerializedName("accountType")
	@Expose
	private String accountType;
	@SerializedName("country")
	@Expose
	private String country;
	@SerializedName("coverPhoto")
	@Expose
	private String coverPhoto;
	@SerializedName("firstName")
	@Expose
	private String firstName;
	@SerializedName("handle")
	@Expose
	private String handle;
	@SerializedName("lastName")
	@Expose
	private String lastName;
	@SerializedName("profilePic")
	@Expose
	private String profilePic;
	@SerializedName("userId")
	@Expose
	private String userId;
	@SerializedName("userType")
	@Expose
	private String userType;

	@SerializedName("myDataStorage")
	@Expose
	private String myDataStorage;

	@SerializedName("myGlobalDataStorage")
	@Expose
	private String myGlobalDataStorage;


	public void inflateWith(Map<String, String> dictionary) {  // only needed on marshalling data from secure storage (automatically done on API call)
		if (dictionary != null) {
			Log.d("inflateWith User ", "");
			for ( String key : dictionary.keySet() ) {
				System.out.println( key );
			}
		}
	}

	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}
	public void setCoverPhoto(String coverPhoto) {
		this.coverPhoto = coverPhoto;
	}

	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {		return userType;	}
	public void setUserType(String userType) {		this.userType = userType;	}

	public String getMyDataStorage() {
		PPManager ppsdk = PPManager.getInstance();
		if(handle != null) {
			String name = ppsdk.androidContext.getResources().getString(R.string.app_name);
			return(handle + "@" + name);
		} else {
			return " ";
		}
	}
	public String getMyGlobalDataStorage() {
		PPManager ppsdk = PPManager.getInstance();
		if (handle != null) {
			String name = ppsdk.androidContext.getResources().getString(R.string.app_name);
			return("globalAppData" + "@" + ppsdk.androidContext.getResources().getString(R.string.app_name));
		} else {
			return "unknown";
		}
	}


	public String getValueForKey(String key) throws IllegalAccessException {
		for (Field field : this.getClass().getDeclaredFields()) {
			field.setAccessible(true); // You might want to set modifier to public first.
			try {
				Object value = field.get(this);
				if ((value != null) && (field.getName() == key)) {
					return value.toString();
				}
			} catch (IllegalAccessException exc) {
				throw exc;      // Rethrow the exception.
			}
		}
		return null;
	}


}