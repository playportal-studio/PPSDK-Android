package com.dynepic.ppsdk_android;

import android.media.Image;
import android.util.Log;

import com.dynepic.ppsdk_android.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dynepic.ppsdk_android.PPWebApi.getApi;

public class PPUserService {

	public HashMap<String, String> userDictionary;
	public Image copyOfProfileImage;
	public Image copyOfCoverImage;


	public PPUserObject getProfile(boolean openBuckets) {
		PPManager ppsdk = PPManager.getInstance();
		String btoken = "Bearer " + ppsdk.accessToken;

		Call<User> call = getApi().getUser(btoken);
		Log.d("getProfile: ", "no parms");
		call.enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				int statusCode = response.code();
				Log.d("status: ", String.valueOf(statusCode));
				if (statusCode == 200) {
					User user = response.body();
					Log.d("getProfile: user:", user.getHandle());

					try {
						ppsdk.PPuserobj.inflateWith(user);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					Log.d("getProfile res: ", String.valueOf(response.body()));
					Log.d("username:", user.getHandle());
					Log.d("my data:", user.getMyDataStorage());
					Log.d("global data:", user.getMyGlobalDataStorage());
					Log.d("userObj: ", user.getUserId());
					ppsdk.setUserPreferences(); // stash into nvram

					if (openBuckets) {
						ArrayList<String> bucketUsers = new ArrayList<>();
						ArrayList<String> emptyBucketUsers = new ArrayList<>();
						bucketUsers.add(user.getUserId());

						ppsdk.PPdatasvc.createBucket(user.getMyDataStorage(), bucketUsers, false, (String bucketName, String key, String data, String error) -> {
							if (error == null) {
								Log.d("getProfileAndBucket:", "opened user data bucket");
								ppsdk.PPdatasvc.createBucket(user.getMyGlobalDataStorage(), emptyBucketUsers, true, (String bucketName2, String key2, String data2, String error2) -> {
									if (error2 == null) {
										Log.d("getProfileAndBucket:", "opened global data bucket");
									}
								});
							}
						});
					}
				}
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Log.e("getProfile error:", "failed with " + t);
			}

		});
		return ppsdk.PPuserobj;
	}

	public void getFriendsProfiles(PPManager.UserProfileCallbackFunction cb)
	{
		PPManager ppsdk = PPManager.getInstance();
		String btoken = "Bearer " + ppsdk.accessToken;
		Call<ArrayList<User>> call = getApi().getFriends(btoken);
		Log.d("getFriends: ", "no parms");

		call.enqueue(new Callback<ArrayList<User>>() {
			@Override
			public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
				int statusCode = response.code();

				Log.d("status: ", String.valueOf(statusCode));
				if (statusCode == 200) {
					ArrayList<User> friends = response.body();
					Log.d("getFriends: user:", friends.toString());
					try {
						Log.d("calling inflate friends from array:", "");
						ppsdk.PPuserobj.inflateFriendsListFromArray(friends);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					Log.d("getFriends response: ", String.valueOf(response.body()));
					cb.f(friends);
				} else {
					cb.f(null);
				}
			}

			@Override
			public void onFailure(Call<ArrayList<User>> call, Throwable t) {
				Log.e("getFriendsProfiles error:", "failed with " + t);
				Log.e("getFriendsProfiles call:",  call.toString());
			}
		});
	}






	public String getMyId() {
		if (userDictionary != null) {
			return userDictionary.get("userId");
		} else {
			return "unknown";
		}
	}

	public String getMyUsername() {
		if (userDictionary != null) {
			return userDictionary.get("handle");
		} else {
			return "unknown";
		}
	}


}