package com.dynepic.ppsdk_android.utils;


import android.content.Context;
import android.util.Log;

import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.utils._CallbackFunction;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dynepic.ppsdk_android.utils._WebApi.getApi;


public class _DataService {

	public void createBucket(String bucketName, ArrayList<String> bucketUsers, Boolean isPublic, Context CONTEXT, _CallbackFunction._Data cb) {

		com.dynepic.ppsdk_android.utils._DevPrefs devPrefs = new com.dynepic.ppsdk_android.utils._DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			Bucket bucketconfig = new Bucket();
			bucketconfig.setPublic(isPublic);
			bucketconfig.setId(bucketName);
			bucketconfig.setUsers(bucketUsers);
			bucketconfig.setData(new HashMap<>());
			Call<Bucket> call = getApi(devPrefs.getBaseUrl()).putData(bucketconfig, btoken);

			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					if((response.code() == 200) || (response.code() == 409))
						Log.d("createBucket response: ", String.valueOf(response.body()));
					cb.f(bucketName, null, null, null);
				}
				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("createBucket error:", "failed with " + t);
					cb.f(bucketName, null, null, t.getMessage());
				}
			});
		}
		cb.f(bucketName, null, null, "Error: failed to create bucket");
	}


	public void readBucket(String bucketName, String key, Context CONTEXT, _CallbackFunction._Data cb)
	{
		Log.d("readBucket: ", bucketName + " and key:" + key);
		Boolean readSuccess = true;
		com.dynepic.ppsdk_android.utils._DevPrefs devPrefs = new com.dynepic.ppsdk_android.utils._DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();

			Map<String, String> queryparms = new HashMap<>();
			queryparms.put("id", bucketName);
			if(key != null) { queryparms.put("key", key); }
			Call<JsonObject> call = getApi(devPrefs.getBaseUrl()).readData(queryparms, btoken);
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if(response.code() == 200) {
						try {
							JsonObject jdata = response.body().getAsJsonObject("data");
							Log.d("value@key: ", key + " : "+ jdata.get(key).toString());
							cb.f(bucketName, key, jdata.getAsJsonObject(key), null);
						} catch(Exception e) {
							cb.f(bucketName, key, null, "Key: "+key+" not found in bucket:"+bucketName+"  Err:"+e.getMessage());
						}
					}
				}
				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e("readBucket error:", "failed with " + t + " on bucket:" + bucketName);
					cb.f(bucketName, key, null, t.getMessage());
				}
			});
		}
	}




	public void writeBucket(String bucketName, String key, JsonObject value, Boolean push, Context CONTEXT, _CallbackFunction._Data cb) {
		Log.d("writeBucket", "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null && value != null) {
			com.dynepic.ppsdk_android.utils._DevPrefs devPrefs = new com.dynepic.ppsdk_android.utils._DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonObject body = new JsonObject();
			body.addProperty("key", key);
			body.add("value", value);
			body.addProperty("id", bucketName);
			body.addProperty("access_token", btoken);

			Call<JsonObject> call = getApi(devPrefs.getBaseUrl()).writeData(body, btoken);
			Log.d("write body: ", body.toString());
			call.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					if(response.code() == 200) {
						JsonObject jdata = response.body().getAsJsonObject("data");
						Log.d("writeBucket res: ", String.valueOf(response.body()));
						cb.f(bucketName, key, value, null);
					}
				}

				@Override
				public void onFailure(Call<JsonObject> call, Throwable t) {
					Log.e("writeBucket error:", "failed with " + t);
					cb.f(bucketName, key, value, t.getMessage());
				}
			});
		} else {
			cb.f(bucketName, key, value, "Error: bucket write failed - invalid parms!");
		}
	}

}