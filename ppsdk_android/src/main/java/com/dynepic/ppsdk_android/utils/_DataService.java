package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dynepic.ppsdk_android.models.Bucket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class _DataService {
	private static final String TAG = "_DataService";
	public _WebApi webApi;

	public _DataService(_WebApi w) {
		Log.d("_DataService() ", "constructor");
		webApi = w;
	}

	public void createBucket(String bucketName, ArrayList<String> bucketUsers, Boolean isPublic, Context CONTEXT, _CallbackFunction._Data cb) {

		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			Bucket bucketconfig = new Bucket();
			bucketconfig.setPublic(isPublic);
			bucketconfig.setId(bucketName);
			bucketconfig.setUsers(bucketUsers);
			bucketconfig.setData(new HashMap<>());
			Call<JsonObject> call = webApi.getApi(devPrefs.getBaseUrl()).putData(bucketconfig, btoken);

			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if((response.code() == 200) || (response.code() == 409))
						Log.d(TAG,"createBucket response: " + String.valueOf(response.body()));
						cb.f(response.body(), null);
					}
				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e(TAG,"createBucket error:" + "failed with " + t);
					cb.f(null, t.getMessage());
				}
			});
		} else {
			cb.f(null, "Error: failed to create bucket - bucket name 'null'");
		}
	}

	public void readBucket(String bucketName, String key, Context CONTEXT, _CallbackFunction._Data cb)
	{
		Log.d(TAG,"readBucket: " + bucketName + " and key:" + key);
		Boolean readSuccess = true;
		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();

			Map<String, String> queryparms = new HashMap<>();
			queryparms.put("id", bucketName);
			if(key != null) { queryparms.put("key", key); }
			Call<JsonObject> call = webApi.getApi(devPrefs.getBaseUrl()).readData(queryparms, btoken);
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if(response.code() == 200) {
						try {

							JsonObject jdata = response.body().getAsJsonObject("data");
							Log.d("jdata.get(key):", jdata.get(key).toString());
							if (jdata.get(key).isJsonPrimitive()) {
								Log.d(TAG, "key is for a primitive... " + "synthesizing JsonObject");
								JsonObject newjo = new JsonObject();
								newjo.add(key, jdata.get(key));
								cb.f(newjo, null);
							} else {
								cb.f(jdata.getAsJsonObject(key), null);
							}
						} catch(Exception e) {
							cb.f(null, "Key: "+key+" not found in bucket:"+bucketName+"  Err:"+e.getMessage());
						}
					}
				}
				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e(TAG,"readBucket error:" + "failed with " + t + " on bucket:" + bucketName);
					cb.f(null, t.getMessage());
				}
			});
		}
	}
	public void write(String bucketName, String key, Boolean value, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		Log.d(TAG,"writeBucket" + "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("key", key);
			body.addProperty("value", value);
			body.addProperty("id", bucketName);
			body.addProperty("access_token", btoken);
			writef(bucketName, body, push, CONTEXT, cb);
		} else {
			cb.f(null, "Invalid parms to bucket write!");
		}
	}
	public void write(String bucketName, String key, String value, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		Log.d(TAG,"writeBucket" + "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("key", key);
			body.addProperty("value", value);
			body.addProperty("id", bucketName);
			body.addProperty("access_token", btoken);
			writef(bucketName, body, push, CONTEXT, cb);
		} else {
			cb.f(null, "Invalid parms to bucket write!");
		}
	}
	public void write(String bucketName, String key, Integer value, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		Log.d(TAG,"writeBucket" + "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("key", key);
			body.addProperty("value", value);
			body.addProperty("id", bucketName);
			body.addProperty("access_token", btoken);
			writef(bucketName, body, push, CONTEXT, cb);
		} else {
			cb.f(null, "Invalid parms to bucket write!");
		}
	}

	public void write(String bucketName, String key, JsonObject value, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		Log.d(TAG,"writeBucket" + "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null && value != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("key", key);
			body.add("value", value);
			body.addProperty("id", bucketName);
			body.addProperty("access_token", btoken);
			writef(bucketName, body, push, CONTEXT, cb);
		} else {
			cb.f(null, "Invalid parms to bucket write!");
		}
	}

	public void writef(String bucketName, JsonObject body, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
		String btoken = "Bearer " + devPrefs.getClientAccessToken();
		Call<JsonObject> call = webApi.getApi(devPrefs.getBaseUrl()).writeData(body, btoken);
		Log.d(TAG,"write body: " + body.toString());
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if(response.code() == 200) {
						JsonObject jdata = response.body().getAsJsonObject("data");
						Log.d(TAG,"writeBucket res: " + String.valueOf(response.body()));
						cb.f(jdata, null);
					}
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e(TAG,"writeBucket error:" + "failed with " + t);
					cb.f(null, t.getMessage());
				}
			});
		}
	}


