package com.dynepic.ppsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dynepic.ppsdk_android.fragments.ssoLoginFragment;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;
import com.dynepic.ppsdk_android.utils._UserPrefs;


/**
 * This class should handle most of the activity within the app.

 * Configure PPManager
 * Show SSO Login Form
 * Get Buckets
 * Set Buckets
 * Get User
 * Set User

 =========
 = Usage =
 =========

 * Initialize your activity's context. Content cannot be referenced from static context.
 * Requires CONTEXT and ACTIVITY_CONTEXT
 *
 * Activity context is normally 'this', or references the controlling activity.
 * The same applies for normal context.
 * IE:
 * 	  Activity ACTIVITY_CONTEXT = this;
 * 	  Context CONTEXT = this;

 PPManager ppManager = new PPManager(CONTEXT, ACTIVITY_CONTEXT);


 * Configure your Client ID, Client Secret, and Redirect URL.
 * This can be checked using isConfigured() - see below.

 ppManager.configure("CLIENT_ID","CLIENT_SECRET","REDIRECT_URL")


 * Call the Single-Sign-On Login page
 * The intent indicates which activity you want to go to after passing the SSO Login

 ppManager.showSSOLogin(INTENT)


 * Check to see if the PPManager has been configured at launch
 * This is normally a good check in case the user has cleared application data.
 * Returns TRUE if the id, secret, and redirect-url are all non-null AND non-empty strings.

 ppManager.isConfigured()

 *
 */


public class PPManager {

	private Context CONTEXT;
	private Activity ACTIVITY;
	private _DevPrefs devPrefs;
	private _UserPrefs userPrefs;

	public PPManager(Context context, Activity activity){
		CONTEXT = context;
		ACTIVITY = activity;
		devPrefs = new _DevPrefs(CONTEXT);
		userPrefs = new _UserPrefs(CONTEXT);
	}

	//region Configuration

	public void configure(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL) {
		Log.d("PPManager.Configure","\nConfiguring PPManager:\nID : "+CLIENT_ID+"\nSEC : "+CLIENT_SECRET+"\nREDIR : "+REDIRECT_URL);
		devPrefs.setClientId(CLIENT_ID);
		devPrefs.setClientSecret(CLIENT_SECRET);
		devPrefs.setClientRedirect(REDIRECT_URL);
    }

    public Boolean isConfigured(){
		return devPrefs.exists();
	}

	public Configuration getConfiguration(){
		return new Configuration();
	}

	public class Configuration {

		public String getClientId() {
			return devPrefs.getClientId();
		}

		public void setClientId(String value) {
			devPrefs.setClientId(value);
		}

		public String getClientSecret() {
			return devPrefs.getClientSecret();
		}

		public void setClientSecret(String value) {
			devPrefs.setClientSecret(value);
		}

		public String getClientRedirect() {
			return devPrefs.getClientRedirect();
		}

		public void setClientRedirect(String value) {
			devPrefs.setClientRedirect(value);
		}

		public String getClientAccessToken() {
			return devPrefs.getClientAccessToken();
		}

		public void setClientAccessToken(String value) {
			devPrefs.setClientAccessToken(value);
		}

		public boolean exists(){
			return devPrefs.exists();
		}

		public void clear(){
			devPrefs.clear();
		}

	}

	//endRegion

	public void showSSOLogin(Intent intent){
		Log.d("PPManager.showSSOLogin","Launching playPORTAL SSO...");
		ssoLoginFragment ssoLoginFragment = new ssoLoginFragment();
		ssoLoginFragment.setNextActivity(intent);
		_DialogFragments.showDialogFragment(ACTIVITY, ssoLoginFragment, true, "SSO");
	}

	public void getUserBuckets(){

	}

	public void getGlobalBucket(){

	}

	public void setUserBucket(){

	}

	public void setGlobalBucket(){

	}

	public void getUser(){

	}

	public void setUser(){

	}




}