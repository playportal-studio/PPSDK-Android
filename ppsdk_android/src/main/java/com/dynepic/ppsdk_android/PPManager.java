package com.dynepic.ppsdk_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.RequestCreator;

import org.json.JSONObject;

import java.util.ArrayList;

import com.dynepic.ppsdk_android.fragments.loadingFragment;
import com.dynepic.ppsdk_android.fragments.ssoLoginFragment;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.models.UserHandler;
import com.dynepic.ppsdk_android.utils._CallbackFunction;
import com.dynepic.ppsdk_android.utils._DataService;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;
import com.dynepic.ppsdk_android.utils._LeaderboardService;
import com.dynepic.ppsdk_android.utils._Notifications;
import com.dynepic.ppsdk_android.utils._PicassoCache;
import com.dynepic.ppsdk_android.utils._UserPrefs;
import com.dynepic.ppsdk_android.utils._WebApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Objects.isNull;

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

 ppManager.configure("CLIENT_ID","CLIENT_SECRET","REDIRECT_URL")

 * Check that this user is configured and authenticated
 * This can be checked using isAuthentidated() - see below.

 * Changing config settings is done through ppManager.getConfiguration()
 * Make sure that ppManager is instantiated.
 * All config settings should be changed this way.

 ppManager.getConfiguration().setClientRedirect("REDIRECT_STRING")

 * Register an authListener to detect changes in authentication state
 ppManager.addAuthListener(Callback  cb);

 * Check authentication status
 ppManager.isAuthenticated()

 * If not authenticated:
 * -- Call the Single-Sign-On Login page
 * -- which includes the intent indicates which activity you want to go to after passing the SSO Login
 ppManager.showSSOLogin(INTENT)

* Else, if authenticated
* -- enter app



 * Getting logged in user data is done through get and set methods
 * Returns a string value of the user data requested
 * User Data should be handled this way:

 ppManager.getUserData().getValue();
 ppManager.getUserData().setValue();

*/

// PPManager is a singleton
public class PPManager {
	private static final String TAG = "PPManager";

	private Context context;
	private Activity activity;
	private _DevPrefs devPrefs;
	private _UserPrefs userPrefs;
	private UserHandler userHandler;
	public _WebApi webApi;
	public _Notifications notifications;
	private _CallbackFunction._Auth authListenerFnx;

	public void setContextAndActivity(Context c, Activity a) {
		Log.d(TAG, "Context:" + c.getPackageName() + " Activity:"+ a.getCallingPackage());
		context = c;
		activity = a;
		devPrefs = new _DevPrefs(context);
		userPrefs = new _UserPrefs(context);
		userHandler = new UserHandler(context);
	}

      // PPManager is a singleton
      private static PPManager ppManager = new PPManager();
      public static PPManager getInstance( ) { return ppManager; }
      private PPManager() {  // A private Constructor prevents any other class from instantiating.
		  Log.d(TAG, "private constructor invoked");
      }


	public void enablePushNotifications() {
		if(isNull(webApi)) {
			webApi = new _WebApi(devPrefs, this::authListener, (status) -> {
				Log.d(TAG, "_WebApi init'd status:" + status.toString());
			});
		}
			notifications = new _Notifications(webApi, context, (Boolean state, String error) -> {
      		Log.d(TAG, "enablePushNotification init state:" + state + " error: " + error);
		});
	}

	public void sendPushNotification(String msg, String receiverId, _CallbackFunction._GenericWithError cb) {
		notifications.send(msg, receiverId, cb);
	}

	// --------------------------------------------------------------------------------
	// Auth
	// invoke apps authListener on an auth status change
	// --------------------------------------------------------------------------------
	public void authListener(Boolean isAuthd) {
		Log.d(TAG,"authListener invoked: " + isAuthd.toString());
		if(authListenerFnx != null) authListenerFnx.f(isAuthd);
	}
	public void addAuthListener(_CallbackFunction._Auth f) {
		Log.d(TAG,"authListener added: " + f.toString());
		authListenerFnx = f;
	}

	// --------------------------------------------------------------------------------
	// Main configuration of the playPORTAL mgr
	// --------------------------------------------------------------------------------
	public void configure(String CLIENT_ID, String CLIENT_SECRET, String REDIRECT_URL, String env, String appName, _CallbackFunction._Generic cb) {

		Log.d("PPManager.Configure", "\nConfiguring PPManager:\nID : " + CLIENT_ID + "\nSEC : " + CLIENT_SECRET + "\nREDIR : " + REDIRECT_URL + "\nEnv :" + env + "\nApp Name :" + appName);
		devPrefs.setClientId(CLIENT_ID);
		devPrefs.setClientSecret(CLIENT_SECRET);
		devPrefs.setClientRedirect(REDIRECT_URL);
		devPrefs.setAppName(appName);

		if (env == "PRODUCTION") {
			devPrefs.setBaseUrl("https://api.playportal.io");
		} else if (env == "DEV") {
			devPrefs.setBaseUrl("https://develop-api.goplayportal.com");
		} else {
//			devPrefs.setBaseUrl("https://sandbox.iokids.net");
			devPrefs.setBaseUrl("https://sandbox.playportal.io");
		}
		Log.d(TAG,"using baseUrl:" + devPrefs.getBaseUrl());
		if(isNull(webApi)) {
			webApi = new _WebApi(devPrefs, this::authListener, (status) -> {
				Log.d(TAG, "_WebApi init'd status:" + status.toString());
				cb.f(status);
			});
		}
	}

	public String getBaseUrl() {
		try {
			return devPrefs.getBaseUrl();
		} catch(Exception e) {
			Log.e(TAG, "attempting to getBaseUrl before playPORTAL initialized.");
			throw e;
		}
	}

	public void logout() {
		devPrefs.clear();
		userPrefs.clear();
    }

	public Boolean isAuthenticated() {
		return devPrefs.exists();
	}

	// Default callback
    private com.squareup.picasso.Callback defaultCallback = new com.squareup.picasso.Callback() {
        @Override
        public void onSuccess() {
            Log.d("Picasso ", "success");
        }

        @Override
        public void onError(Exception e) {
            Log.v("Picasso", "Could not fetch image");
        }
    };

	public void loadImageByIDNoCache(Context context, String imageId, ImageView intoImage) {
		loadImageByIDNoCache(context, imageId, intoImage, defaultCallback);
	}

	public void loadImageByIDNoCache(Context context, String imageId, ImageView intoImage, int width, int height) {
		loadImageByIDNoCache(context, imageId, intoImage, width, height, defaultCallback);
	}

	public void loadImageByIDNoCache(Context context, String imageId, ImageView intoImage, com.squareup.picasso.Callback callback) {
		loadImageByIDNoCache(context, imageId, intoImage, 0, 0, callback);
	}

	public void loadImageByIDNoCache(Context context, String imageId, ImageView intoImage, int width, int height, com.squareup.picasso.Callback callback) {
		RequestCreator rc = _PicassoCache.getPicassoInstance(context)
				.load(PPManager.getInstance().getBaseUrl() + "/image/v1/static/" + imageId)
				// Skips memory cache lookup when processing a request.
				.memoryPolicy(MemoryPolicy.NO_CACHE)
				// Skips checking the disk cache and forces loading through the network
				.networkPolicy(NetworkPolicy.NO_CACHE);
		// No size when size is below or equal zero
		if (width > 0 && height > 0) {
			rc = rc.resize(width, height);
		}
		rc.into(intoImage, callback);
	}

	public void loadImageByID(Context context, String imageId, ImageView intoImage) {
		loadImageByID(context, imageId, intoImage, defaultCallback);
	}

	public void loadImageByID(Context context, String imageId, int width, int height, ImageView intoImage) {
		loadImageByID(context, imageId, intoImage, width, height, defaultCallback);
	}

	public void loadImageByID(Context context, String imageId, ImageView intoImage, com.squareup.picasso.Callback callback) {
		loadImageByID(context, imageId, intoImage, 0, 0, callback);
	}

	public void loadImageByID(Context context, String imageId, ImageView intoImage, int width, int height, com.squareup.picasso.Callback callback) {
		RequestCreator rc = _PicassoCache.getPicassoInstance(context)
				.load(PPManager.getInstance().getBaseUrl() + "/image/v1/static/" + imageId);
		// No size when size is below or equal zero
		if (width > 0 && height > 0) {
			rc = rc.resize(width, height);
		}
		// Forces the request through the disk cache only, skipping network.
		rc.networkPolicy(NetworkPolicy.OFFLINE)
				// Skips storing the result into the disk cache.
				.networkPolicy(NetworkPolicy.NO_STORE)
				.into(intoImage, new com.squareup.picasso.Callback() {
					@Override
					public void onSuccess() {
						callback.onSuccess();
					}

					@Override
					public void onError(Exception e) {
						//Try again online if cache failed
						loadImageByIDNoCache(context, imageId, intoImage, callback);
					}
				});
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

	// --------------------------------------------------------------------------------
	//region UserData
    // --------------------------------------------------------------------------------
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

		public String myData() {
			return userPrefs.getHandle() + "@" + devPrefs.getAppName();
		}

		public String globalData() {
			return "globalAppData@" + devPrefs.getAppName();
		}
	}

	public void updateUserFromWeb(ssoLoginFragment ssoLoginFragment, loadingFragment loading, Activity ACTIVITY_CONTEXT,Intent NEXT_INTENT) {
		_DevPrefs settings = new _DevPrefs(context);
		String btoken = "Bearer " + settings.getClientAccessToken();
		Log.i("SSO_LOGIN", "Requesting User Data");
		Log.i("SSO_LOGIN base url:", getBaseUrl() + " app:" + devPrefs.getAppName());


		//region Call User Data
		Call<User> call = webApi.getApi(getBaseUrl()).getUser(btoken);
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				loading.dismiss();
				if (response.code() == 200) {
					System.out.println(response.body());
					User userObject = response.body();
					userHandler.populateUserData(userObject);
					if(!userHandler.getHandle().equals("")){
						Log.i(TAG,"SSO_LOGIN" + "UserData Retrieved\n"+ userHandler.toString());
						try{
							context.startActivity(NEXT_INTENT);
							ACTIVITY_CONTEXT.finish();
							ssoLoginFragment.dismiss();
						}catch (Exception e){
							e.printStackTrace();
							Log.e(TAG," SSO_LOGIN_ERR" +"Did you specify context, or an intent for your next activity?");
							Log.e(TAG," SSO_LOGIN_ERR" +"Error in class: "+ACTIVITY_CONTEXT.getLocalClassName());
							Log.e(TAG," SSO_LOGIN_ERR" +"Intent is: "+NEXT_INTENT);
						}

					}
				} else {
					//Dialogs.ShowDialog(LoginError())
					//Dialogs.ShowDialog(SecurityError())
					Log.e(TAG," SSO_LOGIN_ERR" + "Error getting user data.");
					Log.e(TAG," SSO_LOGIN_ERR" + "Response code is : "+response.code());
					Log.e(TAG," SSO_LOGIN_ERR" + "Response message is : "+response.message());
					//Kick back to login

				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Log.e(TAG,"SSO_LOGIN_ERR" + "Request failed with throwable: " + t);
				//Call Failed. Try again
				//Kick back to login
			}
		});
		//endregion
	}



	// ------------------------------------------------------------------------------
	// Methods to support image download (e.g. user profile image)
	// ------------------------------------------------------------------------------
	public String getTestImageParms() {
		return getBaseUrl() + "/image/v1/static/12355.jpg";
	}

	public String getPicassoParms() {
		return getBaseUrl() + "/user/v1/my/profile/picture";
	}

	public OkHttp3Downloader imageDownloader() {
		return webApi.createDownloader();
    }




	// ------------------------------------------------------------------------------
	// Friends
	// ------------------------------------------------------------------------------
	public FriendsService friends() {
		return new FriendsService();
	}

	public class FriendsService {
		ArrayList<User> friendsList;

		FriendsService() {
		}

		public ArrayList<User> get(_CallbackFunction._Friends cb) {
			Call<ArrayList<User>> friendsCall = webApi.getApi(getBaseUrl()).getFriends(devPrefs.getClientAccessToken());
			friendsCall.enqueue(new Callback<ArrayList<User>>() {
				@Override
				public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
					if (response.code() == 200) {
						System.out.println(response.body());
						friendsList = response.body();
						cb.f(friendsList, null);
					} else {
						Log.e(TAG," GET_FRIENDS_ERR" + "Error getting friends data.");
						Log.e(TAG," GET_FRIENDS_ERR" +  "Response code is : " + response.code());
						Log.e(TAG," GET_FRIENDS_ERR" + "Response message is : " + response.message());
					}
				}

				@Override
				public void onFailure(Call<ArrayList<User>> call, Throwable t) {
					Log.e(TAG,"GET_FRIENDS_ERR" + "Request failed with throwable: " + t);
				}
			});
			return friendsList;
		}
	}


	// ------------------------------------------------------------------------------
	// Lightning Data
	// ------------------------------------------------------------------------------
	private static _DataService appDataService;
	public DataService data() {
		return new DataService(webApi);
	}

	public class DataService {

		DataService(_WebApi webApi) {
			if (appDataService == null) {
				appDataService = new _DataService(webApi);
				createBucket(userPrefs.myData(devPrefs.getAppName()), new ArrayList<String>(), false, context, (JsonObject value, String error) -> {
					if (error != null) {
						Log.e(TAG,"AppData create error:" + error + " for bucket: " + userPrefs.myData(devPrefs.getAppName()));
					} else {
						Log.d(TAG,"Created AppData" + userPrefs.myData(devPrefs.getAppName()));
					}
				});
				createBucket(userPrefs.globalData(devPrefs.getAppName()), new ArrayList<String>(), true, context, (JsonObject value, String error) -> {
					if (error != null) {
						Log.e(TAG,"GlobalAppData create error:" + error + " for bucket: " + userPrefs.globalData(devPrefs.getAppName()));
					} else {
						Log.d(TAG,"Created GlobalAppData" + userPrefs.globalData(devPrefs.getAppName()));
					}
				});
			}
		}

		public void read(String bucketname, String key, _CallbackFunction._Data cb)  {
			Log.d(TAG,"DataService readData bucket:" + bucketname + " key:" + key);
			appDataService.readBucket(bucketname, key, context, cb);
		}

		public void write(String bucketname, String key, Boolean value, _CallbackFunction._Data cb ) {
			Log.d(TAG,"write boolean:" + value.toString());
			appDataService.write(bucketname, key, value, false, context, cb);
		}
		public void write(String bucketname, String key, String value, _CallbackFunction._Data cb ) {
			Log.d(TAG,"write string:" + value);
			appDataService.write(bucketname, key, value, false, context, cb);
		}
		public void write(String bucketname, String key, Integer value, _CallbackFunction._Data cb ) {
			Log.d(TAG,"write Integer:" + value.toString());
			appDataService.write(bucketname, key, value, false, context, cb);
		}
		public void write(String bucketname, String key, JsonObject value, _CallbackFunction._Data cb ) {
			Log.d(TAG, "write JsonObject:" + value.toString());
			appDataService.write(bucketname, key, value, false, context, cb);
		}
		public void write(String bucketname, String key, JSONObject value, _CallbackFunction._Data cb ) {
			Log.d(TAG, "write JsonObject:" + value.toString());
			appDataService.write(bucketname, key, value.toString(), false, context, cb);
		}



		public void createBucket(String bucketname, ArrayList<String> bucketUsers, Boolean isPublic, Context CONTEXT, _CallbackFunction._Data cb) {
			appDataService.createBucket(bucketname, bucketUsers, isPublic, CONTEXT, cb);
		}
	}




	// -----------------------------------------------------------------------------
	// Leaderboard API
	// -----------------------------------------------------------------------------
	private static _LeaderboardService appLeaderboardService;
	public LeaderboardService leaderboard() {
		return new LeaderboardService(webApi);
	}

	public class LeaderboardService {
		LeaderboardService(_WebApi webApi1) {
			if (appLeaderboardService == null) {
				appLeaderboardService = new _LeaderboardService(webApi);
			}
		}

		public void get(Integer page, Integer limit, String categories, Context CONTEXT, _CallbackFunction._Leaderboard cb) {
			appLeaderboardService.get(page, limit, categories, CONTEXT, cb);
		}


		public void update(Integer score, ArrayList<String> categories, Context CONTEXT, _CallbackFunction._Leaderboard cb) {
			appLeaderboardService.update(score, categories, CONTEXT, cb);
		}
	}






	// ------------------------------------------------------------------------------------------
	// Display SSO Login
	// ------------------------------------------------------------------------------------------
	public void showSSOLogin(Intent intent) {
		Log.d("PPManager.showSSOLogin", "Launching playPORTAL SSO...");
		ssoLoginFragment ssoLoginFragment = new ssoLoginFragment();
		ssoLoginFragment.setNextActivity(intent);
		_DialogFragments.showDialogFragment(activity, ssoLoginFragment, true, "SSO");
	}
}
