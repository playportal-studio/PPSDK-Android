package com.dynepic.ppsdk_android;

import android.util.Log;

import com.dynepic.ppsdk_android.models.Bucket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dynepic.ppsdk_android.PPWebApi.getApi;

public class PPDataService {


    public void createBucket(String bucketName, List<String> bucketUsers, Boolean isPublic, PPManager.CallbackFunction cb)
	{
		if(bucketName != null) {
			Log.d("bucket create name: ", bucketName);

			PPManager ppsdk = PPManager.getInstance();
			String btoken = "Bearer " + ppsdk.accessToken;

			Map<String, String> body = new HashMap<>();
			body.put("public", isPublic.toString());
			body.put("id", bucketName);
			String usrstr;
			String datastr;
			if (isPublic) {
				usrstr = "[]";
				body.put("users", usrstr);
			} else {
				usrstr = "[" + bucketUsers.get(0) + "]";
				body.put("users", usrstr);
//				datastr = "{id:" + ppsdk.PPuserobj.myUserObject.getUserId() + "}";
			}
			datastr = "{}";
			body.put("data", datastr);
			Log.d("body parms: ", body.toString());

			Call<Bucket> call = getApi().putData(body, btoken);
			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					int statusCode = response.code();
					Log.d("createBucket status: ", String.valueOf(statusCode));
					Log.d("raw response:", response.raw().toString());

					if((statusCode == 200) || (statusCode == 409)) {
						Bucket bucket = response.body();
						Log.d("createBucket res: ", String.valueOf(response.body()));
						cb.fse(bucketName, null, null, null);
					} else if(statusCode == 401) {
						Log.d("createBucket 401", " refresh access token and try again");
						if(ppsdk.refreshAccessToken()) { 	// retry operation
							Call<Bucket> retryCall = getApi().putData(body, btoken);
							retryCall.enqueue(new Callback<Bucket>() {
								@Override
								public void onResponse(Call<Bucket> retryCall, Response<Bucket> response) {
									int statusCode = response.code();
									Log.d("createBucket retry status: ", String.valueOf(statusCode));
									if((statusCode == 200) || (statusCode == 409)) {
										Bucket bucket = response.body();
										Log.d("createBucket retry res: ", String.valueOf(response.body()));
										cb.fse(bucketName, null, null, null);
									}
								}

								@Override
								public void onFailure(Call<Bucket> retryCall, Throwable t) {
									Log.e("createBucket retry error:", "failed with " + t);
									cb.fse(bucketName, null, null, "Error: failed to create bucket");
								}
							});
						}
					}
				}

				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("createBucket error:", "failed with " + t);
					cb.fse(bucketName, null, null, "Error: failed to create bucket");
				}
			});
		}
		cb.fse(bucketName, null, null, "Error: failed to create bucket");
	}


	public void readBucket(String bucketName, String key, PPManager.CallbackFunction cb)
	{
		Log.d("readBucket: ", bucketName + " and key:" + key);
		Boolean readSuccess = true;

		if(bucketName != null) {
			PPManager ppsdk = PPManager.getInstance();
			String btoken = "Bearer " + ppsdk.accessToken;

			Map<String, String> queryparms = new HashMap<>();
			queryparms.put("id", bucketName);
			if(key != null) { queryparms.put("key", key); }
			Call<Bucket> call = getApi().readData(queryparms, btoken);
			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					int statusCode = response.code();
					Log.d("raw response:", response.raw().toString());
					if (statusCode == 200) {
						Bucket bucket = response.body();
						HashMap<String, String> bucketData = bucket != null ? bucket.getData() : null;
						Log.d("readBucket raw data:", bucketData.get(key).toString());
						cb.fse(bucketName, key, bucketData.get(key).toString(), null);
					} else if(statusCode == 401) { // refreshToken and try again
						if(ppsdk.refreshAccessToken()) {    // retry operation
							Call<Bucket> retryCall = getApi().readData(queryparms, btoken);
							retryCall.enqueue(new Callback<Bucket>() {
								@Override
								public void onResponse(Call<Bucket> retryCall, Response<Bucket> response) {
									int statusCode = response.code();
									Log.d("raw response:", response.raw().toString());
									if (statusCode == 200) {
										Bucket bucket = response.body();
										Log.d("readBucket res: ", String.valueOf(response.body()));
										HashMap<String, String> bucketData = bucket != null ? bucket.getData() : null;
										cb.fse(bucketName, key, bucketData.get(key).toString(), null);
									}
								}

								@Override
								public void onFailure(Call<Bucket> retryCall, Throwable t) {
									Log.e("retry readBucket error:", "failed with " + t + " on bucket:" + bucketName);
									cb.fse(bucketName, key, null, t.getMessage());
								}
							});
						}
					}
				}

				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("readBucket error:", "failed with " + t + " on bucket:" + bucketName);
//					cb.f(bucketName, key, null, t.getMessage());
					cb.fse(bucketName, key, null, t.getMessage());
				}
			});
		}
	}




	public void writeBucket(String bucketName, String key, String value, Boolean push, PPManager.CallbackFunction cb) {
		Log.d("writeBucket", "bucket: " + bucketName + " key:" + key + " value:" + value);
		PPManager ppsdk = PPManager.getInstance();
		String btoken = "Bearer " + ppsdk.accessToken;
		HashMap<String, String> map = new HashMap<>();
		map.put(key, value);

		if (bucketName != null && key != null && value != null) {
			Map<String, String> body = new HashMap<>();
			body.put("id", bucketName);
			body.put("key", key);
			body.put("value", value);
			body.put("access_token", btoken);
			Log.d("body parms: ", body.toString());

			Call<Bucket> call = getApi().writeData(body, btoken);
			Log.d("write body: ", body.toString());
			call.enqueue(new Callback<Bucket>() {
				@Override
				public void onResponse(Call<Bucket> call, Response<Bucket> response) {
					int statusCode = response.code();
					Log.d("raw response:", response.raw().toString());

					if(statusCode == 200) {
						Bucket bucket = response.body();
						Log.d("writeBucket res: ", String.valueOf(response.body()));
						cb.fse(bucketName, key, value, null);
					} else if(statusCode == 401) {
						if(ppsdk.refreshAccessToken()) {
							Call<Bucket> retryCall = getApi().writeData(body, btoken);
							retryCall.enqueue(new Callback<Bucket>() {
								@Override
								public void onResponse(Call<Bucket> retryCall, Response<Bucket> response) {
									int statusCode = response.code();
									Log.d("retry writeBucket response:", response.raw().toString());
									if (statusCode == 200) {
										Bucket bucket = response.body();
										Log.d("retry writeBucket res: ", String.valueOf(response.body()));
										cb.fse(bucketName, key, value, null);
									}
								}

								@Override
								public void onFailure(Call<Bucket> retryCall, Throwable t) {
									Log.e("retry  writeBucket error:", "failed with " + t);
									cb.fse(bucketName, key, value, "Error: retry bucket write failed");
								}
							});
						}
					}
				}

				@Override
				public void onFailure(Call<Bucket> call, Throwable t) {
					Log.e("writeBucket error:", "failed with " + t);
					cb.fse(bucketName, key, value, "Error: bucket write failed");
				}
			});
		} else {
			cb.fse(bucketName, key, value, "Error: bucket write failed");
		}
	}

}