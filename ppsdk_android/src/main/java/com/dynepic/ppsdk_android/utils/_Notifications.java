package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.firebase.messaging.RemoteMessage;

public class _Notifications extends FirebaseMessagingService {

	public FirebaseMessagingService fb;

	private static final String TAG = "_Notifications";
	public _WebApi webApi;
	private _DevPrefs devPrefs;

	public _Notifications() {
		Log.d(TAG, "Generic constructor invoked");
	};

	public _Notifications(_WebApi w, Context context, _CallbackFunction._GenericWithError cb) {
		Log.d(TAG, "Custom constructor");
		webApi = w;

		devPrefs = new _DevPrefs(context);

		fb = new FirebaseMessagingService();

		// retrieve device token
		FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
			@Override
			public void onComplete(@NonNull Task<InstanceIdResult> task) {
				if (!task.isSuccessful()) {
					Log.w(TAG, "getInstanceId failed", task.getException());
					return;
				}

				String token = task.getResult().getToken();
				devPrefs.setDeviceToken(token);

				Log.d(TAG, " token:" + token);

				registerToken(token, (Boolean status, String error) -> {
					if(status) {
						Log.d(TAG, "Success registering token");
					} else {
						Log.e(TAG, "Error registering token:" + error);
					}
					return;
				});
			}
		});

		if(fb != null) {
			cb.f(true, "Success instantiating push service");
		} else {
			cb.f(false, "Error: instantiating push service");
		}

	}




	public void onMessageReceived(RemoteMessage msg) {
		if(msg != null) {
			Log.d(TAG, "Msg: " + msg.toString());
		}
	}


//  Called when InstanceID / device token is updated.
	public void onNewToken(String token) {
		Log.d(TAG, "new device token: " + token);
		registerToken(token, (status, msg) -> {
			if(!status) Log.d(TAG, "Error capturing new device token:" + msg);
		});
	}

// Send a notification msg to receiver
	public void send(String msg, String receiverId, _CallbackFunction._GenericWithError cb) {
		Log.d(TAG, "send push msg:"+ msg + " to receiverId:" + receiverId);
		String btoken = "Bearer " + devPrefs.getClientAccessToken();
		JsonObject body = new JsonObject();
		body.addProperty("text", msg);
		body.addProperty("receiver", receiverId);
		body.addProperty("persist", false);
		Call<JsonObject> call = webApi.getApi(devPrefs.getBaseUrl()).send(body, btoken);
		call.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				if (response.code() == 200) {
					cb.f(true, null);
				} else {
					cb.f(false, response.message());
				}
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t) {
				Log.e("push send error:", "failed with " + t);
				cb.f(false, t.getMessage());
			}
		});
	}

	// Register device token with the server
	private void registerToken(String token, _CallbackFunction._GenericWithError cb) {
		Log.d(TAG, "registerToken" + " token: " + token);
		if (token != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("deviceToken", token);
			body.addProperty("refreshToken", devPrefs.getClientRefreshToken());

			Call<JsonObject> call = webApi.getApi(devPrefs.getBaseUrl()).registerDeviceToken(body, btoken);
			Log.d(TAG, "registerToken body: " + body.toString());
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if (response.code() == 200) {
						Log.d(TAG,"registerToken res: " + String.valueOf(response.body()));
						cb.f(true, null);
					}
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e(TAG,"registerToken error:" + "failed with " + t);
					cb.f(false, t.getMessage());
				}
			});
		} else {
			cb.f(false, "Error: null token" );
		}
	}
}


