package com.dynepic.ppsdk_android;

import android.content.Context;

import com.dynepic.ppsdk_android.models.UserHandler;
import com.dynepic.ppsdk_android.utils._UserPrefs;

import java.util.ArrayList;

public class PPUserService {

	private Context CONTEXT;
	private _UserPrefs userPrefs;

	public PPUserService(Context context){
		CONTEXT = context;
	}

	public Boolean hasUser(){
		userPrefs = new _UserPrefs(CONTEXT);
		return userPrefs.exists();
	}

	public UserHandler getUser(Context CONTEXT){

		return null;
	}

	public ArrayList<String> getUserFriends(){

		return null;
	}





}