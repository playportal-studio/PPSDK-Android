package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.utils._CallbackFunction;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import static com.dynepic.ppsdk_android.utils._WebApi.getApi;


public class _DataService {

	public void createBucket(String bucketName, ArrayList<String> bucketUsers, Boolean isPublic, _CallbackFunction cb, Context CONTEXT) {

		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			Bucket bucketconfig = new Bucket();
			bucketconfig.setPublic(isPublic);
			bucketconfig.setId(bucketName);
			bucketconfig.setUsers(bucketUsers);
			bucketconfig.setData(new HashMap<>());
			Call<Bucket> call = getApi(null, devPrefs.getBaseUrl()).putData(bucketconfig, btoken);

			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					if((response.code() == 200) || (response.code() == 409))
						Log.d("createBucket response: ", String.valueOf(response.body()));
						cb.cbf(bucketName, null, null, null);
					}
				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("createBucket error:", "failed with " + t);
					cb.cbf(bucketName, null, null, "Error: failed to create bucket");
				}
			});
		}
		cb.cbf(bucketName, null, null, "Error: failed to create bucket");
	}


	public void readBucket(String bucketName, String key, _CallbackFunction cb, Context CONTEXT)
	{
		Log.d("readBucket: ", bucketName + " and key:" + key);
		Boolean readSuccess = true;
		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);

		if(bucketName != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();

			Map<String, String> queryparms = new HashMap<>();
			queryparms.put("id", bucketName);
			if(key != null) { queryparms.put("key", key); }
			Call<Bucket> call = getApi(null, devPrefs.getBaseUrl()).readData(queryparms, btoken);
			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					if(response.code() == 200) {
						Bucket bucket = response.body();
						HashMap<String, String> bucketData = bucket != null ? bucket.getData() : null;
						cb.cbf(bucketName, key, bucketData.get(key).toString(), null);
					}
				}
				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("readBucket error:", "failed with " + t + " on bucket:" + bucketName);
					cb.cbf(bucketName, key, null, t.getMessage());
				}
			});
		}
	}




	public void writeBucket(String bucketName, String key, String value, Boolean push, _CallbackFunction cb, Context CONTEXT) {
		Log.d("writeBucket", "bucket: " + bucketName + " key:" + key + " value:" + value);
		if (bucketName != null && key != null && value != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			HashMap<String, String> body = new HashMap<String, String>()
				{{
					put(key, value);
					put("id", bucketName);
					put("key", key);
					put("value", value);
					put("access_token", btoken);
				}};

			Call<Bucket> call = getApi(null, devPrefs.getBaseUrl()).writeData(body, btoken);
			Log.d("write body: ", body.toString());
			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					if(response.code() == 200) {
						Bucket bucket = response.body();
						Log.d("writeBucket res: ", String.valueOf(response.body()));
						cb.cbf(bucketName, key, value, null);
					}
				}

				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("writeBucket error:", "failed with " + t);
					cb.cbf(bucketName, key, value, "Error: bucket write failed");
				}
			});
		} else {
			cb.cbf(bucketName, key, value, "Error: bucket write failed");
		}
	}

}

