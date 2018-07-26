package com.dynepic.ppsdk_android;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dynepic.ppsdk_android.fragments.ssoLoginFragment;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._CallbackFunction;
import com.dynepic.ppsdk_android.utils._DataService;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;
import com.dynepic.ppsdk_android.utils._UserPrefs;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.Interceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dynepic.ppsdk_android.utils._WebApi.getApi;

/**
 * This class should handle most of the activity within the app.

 * Configure PPManager
 * Show SSO Login Form
 * Get Buckets
 * Set Buckets
 * Get User
 * Set User

 ===============
 = Basic Usage =
 ===============

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


 * Changing config settings is done through ppManager.getConfiguration()
 * Make sure that ppManager is instantiated.
 * All config settings should be changed this way.

 ppManager.getConfiguration().setClientRedirect("REDIRECT_STRING")


 * Call the Single-Sign-On Login page
 * The intent indicates which activity you want to go to after passing the SSO Login

 ppManager.showSSOLogin(INTENT)


 * Check to see if the PPManager has been configured at launch
 * This is normally a good check in case the user has cleared application data.
 * Returns TRUE if the id, secret, and redirect-url are all non-null AND non-empty strings.

 ppManager.isConfigured()


 * Getting logged in user data is done through get and set methods
 * Returns a string value of the user data requested
 * User Data should be handled this way:

 ppManager.getUserData().getValue();
 ppManager.getUserData().setValue();



 ===================
 = Friends Request =
 ===================

 * In your controlling activity or class, you need to specify "implements"

 public class YOUR_CLASS_NAME implements PPManager.FriendsService.onFriendsResponse {}

 * Create request, specify activity, delegate, execute request

 PPManager.FriendsService.getFriends friendsRequest = new PPManager.FriendsService.getFriends(ACTIVITY);
 friendsRequest.delegate = this;
 friendsRequest.execute((Void) null);

 * Create @Override method to get results after the Async Call has been completed
 * Returns an ArrayList of Strings of user Handles

 /@Override
 public void onFriendsResponse(ArrayList<String> output) {
 //update your UI based on response
 }

 *
 */


public class PPManager {

	private Context CONTEXT;
	private Activity ACTIVITY;
	private _DevPrefs devPrefs;
	private _UserPrefs userPrefs;

	public PPManager(Context context, Activity activity) {
		CONTEXT = context;
		ACTIVITY = activity;
		devPrefs = new _DevPrefs(CONTEXT);
		userPrefs = new _UserPrefs(CONTEXT);
	}

	//region Configuration


	public void configure(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL, String env, String appName) {

		Log.d("PPManager.Configure", "\nConfiguring PPManager:\nID : " + CLIENT_ID + "\nSEC : " + CLIENT_SECRET + "\nREDIR : " + REDIRECT_URL + "\nEnv :" + env + "\nApp Name :" + appName);
		devPrefs.setClientId(CLIENT_ID);
		devPrefs.setClientSecret(CLIENT_SECRET);
		devPrefs.setClientRedirect(REDIRECT_URL);
		devPrefs.setAppName(appName);

		if (env == "PRODUCTION") {
			devPrefs.setBaseUrl("https://api.playportal.io");
		} else {
			devPrefs.setBaseUrl("https://sandbox.iokids.net");
		}


	}

	public Boolean isConfigured() {
		return devPrefs.exists();
	}

	public Configuration getConfiguration() {
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

		public String getClientRefreshToken() {
			return devPrefs.getClientRefreshToken();
		}

		public void setClientRefreshToken(String value) {
			devPrefs.setClientRefreshToken(value);
		}

		public void setAppName(String value) {
			devPrefs.setAppName(value);
		}

		public String getAppName() {
			return devPrefs.getAppName();
		}

		public boolean exists() {
			return devPrefs.exists();
		}

		public void clear() {
			devPrefs.clear();
		}
	}
	//endregion

	//region UserData
	public UserData getUserData() {
		return new UserData();
	}

	public class UserData {

		public Boolean hasUser() {
			return userPrefs.exists();
		}

		public String getAccountType() {
			return userPrefs.getAccountType();
		}

		public String getCountry() {
			return userPrefs.getCountry();
		}

		public String getCoverPhoto() {
			return userPrefs.getCoverPhoto();
		}

		public String getFirstName() {
			return userPrefs.getFirstName();
		}

		public String getHandle() {
			return userPrefs.getHandle();
		}

		public String getLastName() {
			return userPrefs.getLastName();
		}

		public String getProfilePic() {
			return userPrefs.getProfilePic();
		}

		public String getUserId() {
			return userPrefs.getUserId();
		}

		public String getUserType() {
			return userPrefs.getUserType();
		}

		public String getMyDataStorage() {
			return userPrefs.getHandle() + "@" + devPrefs.getAppName();
//			return userPrefs.getMyDataStorage(devPrefs.getAppName());
		}

		public String getMyGlobalDataStorage() {
			return "globalAppData@" + devPrefs.getAppName();
//			return userPrefs.getMyGlobalDataStorage(devPrefs.getAppName());
		}
		//This returns users as a string, not as user objects.
		// sharedPrefs has issues with storing objects.
//		public ArrayList<String> getStoredFriendData(){
//			if(userPrefs.getFriendData()!=null){
//				return userPrefs.getFriendData();
//			}
//			else{
//				return new ArrayList<>();
//			}
//		}

	}
	//endregion

	public FriendsService getFriendsManager() {
		return new FriendsService();
	}

	public class FriendsService {

		ArrayList<User> friendsList;

		FriendsService() {
		}

		public ArrayList<User> getFriendsData(_CallbackFunction._Friends cb) {
			Call<ArrayList<User>> friendsCall = getApi(devPrefs.getBaseUrl()).getFriends(devPrefs.getClientAccessToken());
			friendsCall.enqueue(new Callback<ArrayList<User>>() {
				@Override
				public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
					if (response.code() == 200) {
						System.out.println(response.body());
						friendsList = response.body();
						cb.f(friendsList, null);
					} else {
						Log.e(" GET_FRIENDS_ERR", "Error getting friends data.");
						Log.e(" GET_FRIENDS_ERR", "Response code is : " + response.code());
						Log.e(" GET_FRIENDS_ERR", "Response message is : " + response.message());
					}
				}

				@Override
				public void onFailure(Call<ArrayList<User>> call, Throwable t) {
					Log.e("GET_FRIENDS_ERR", "Request failed with throwable: " + t);
				}
			});
			return friendsList;
		}
	}

//		public interface FriendsResponse {
//			void onFriendsResponse(ArrayList<String> output);
//		}
//
//		public static class getFriends extends AsyncTask<Void, Void, ArrayList<String>> {
//
//			public FriendsResponse delegate;
//			private ArrayList<User> friendsList;
//			private ArrayList<String> allFriendsHandles;
//			private final WeakReference<Activity> weakActivity;
//
//			public getFriends(Activity activity){
//				weakActivity = new WeakReference<>(activity);;
//			}
//
//			@Override
//			protected ArrayList<String> doInBackground(Void... params) {
//				_DevPrefs devPrefs = new _DevPrefs(weakActivity.get());
//				allFriendsHandles = new ArrayList<>();
//				Call<ArrayList<User>> friendsCall = getApi(weakActivity.get()).getFriends(devPrefs.getClientAccessToken());
//				friendsCall.enqueue(new Callback<ArrayList<User>>() {
//					@Override
//					public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//						if (response.code() == 200) {
//							System.out.println(response.body());
//							friendsList = response.body();
//							for (int i=0;i>=friendsList.size(); i++){
//								allFriendsHandles.add(friendsList.get(i).getHandle());
//							}
//						}
//						else{
//							Log.e(" GET_FRIENDS_ERR","Error getting friends data.");
//							Log.e(" GET_FRIENDS_ERR","Response code is : "+response.code());
//							Log.e(" GET_FRIENDS_ERR","Response message is : "+response.message());
//						}
//					}
//
//					@Override
//					public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//						Log.e("GET_FRIENDS_ERR", "Request failed with throwable: " + t);
//					}
//				});
//				return allFriendsHandles;
//			}
//
//			@Override
//			protected void onPostExecute(final ArrayList<String> result) {
//				delegate.onFriendsResponse(result);
//			}
//
//			@Override
//			protected void onCancelled() {
//			}
//		}



	private static _DataService appDataService = new _DataService();
	public DataService getDataManager() {
		return new DataService();
	}

	public class DataService {

		DataService() {
			if (appDataService == null) {
				appDataService = new _DataService();
				appDataService.createBucket(userPrefs.getMyDataStorage(devPrefs.getAppName()), new ArrayList<String>(), false, CONTEXT, (String bucketName, String key, JsonObject value, String error) -> {
					if (error != null) {
						Log.e("AppData create error:", error + " for bucket: " + userPrefs.getMyDataStorage(devPrefs.getAppName()));
					} else {
						Log.d("Created AppData", userPrefs.getMyDataStorage(devPrefs.getAppName()));
					}
				});
				appDataService.createBucket(userPrefs.getMyGlobalDataStorage(devPrefs.getAppName()), new ArrayList<String>(), true, CONTEXT, (String bucketName, String key, JsonObject value, String error) -> {
					if (error != null) {
						Log.e("GlobalAppData create error:", error + " for bucket: " + userPrefs.getMyGlobalDataStorage(devPrefs.getAppName()));
					} else {
						Log.d("Created GlobalAppData", userPrefs.getMyGlobalDataStorage(devPrefs.getAppName()));
					}
				});
			}
		}

		public void readData(String bucketname, String key, _CallbackFunction._Data cb)  {
			Log.d("DataService readData bucket:", bucketname + " key:" + key);
			appDataService.readBucket(bucketname, key, CONTEXT, cb);
		}

		//		public void writeData(String bucketname, String key, String value, _CallbackFunction._Data cb ) {
		public void writeData(String bucketname, String key, JsonObject value, _CallbackFunction._Data cb ) {
			appDataService.writeBucket(bucketname, key, value, false, CONTEXT, cb);
		}

		public void createBucket(String bucketname, ArrayList<String> bucketUsers, Boolean isPublic, _CallbackFunction._Data cb, Context CONTEXT) {

		}
	}

	private void BucketDataService() {
	}


	public void showSSOLogin(Intent intent) {
		Log.d("PPManager.showSSOLogin", "Launching playPORTAL SSO...");
		ssoLoginFragment ssoLoginFragment = new ssoLoginFragment();
		ssoLoginFragment.setNextActivity(intent);
		_DialogFragments.showDialogFragment(ACTIVITY, ssoLoginFragment, true, "SSO");
	}

}