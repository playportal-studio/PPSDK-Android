package com.dynepic.ppsdk_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


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


	public String getAccountType() {
		return accountType;
	}

	public String getCountry() {
		return country;
	}

	public String getCoverPhoto() {
		return coverPhoto;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getHandle() {
		return handle;
	}

	public String getLastName() {
		return lastName;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserType() {
		return userType;
	}

	public String getMyDataStorage() {
		return myDataStorage;
	}

	public String getMyGlobalDataStorage() {
		return myGlobalDataStorage;
	}


}