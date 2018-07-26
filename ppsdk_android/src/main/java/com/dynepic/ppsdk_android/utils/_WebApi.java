package com.dynepic.ppsdk_android.utils;


import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.models.Tokens;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._DevPrefs;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

//import okhttp3.Response;


public class _WebApi {
	private static _DevPrefs devPrefs;
	private static Context CONTEXT;
	public void setContext(Context c) { CONTEXT = c; }
	public _DevPrefs getDevPrefs() {
		if (devPrefs == null) devPrefs = new _DevPrefs(CONTEXT);
		return devPrefs;
	}

	private static class customInterceptor implements Interceptor {
		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			Request request = chain.request();
			okhttp3.Response response = chain.proceed(request);

			if (response.code()!=409 || response.code()!=200){
				//ToDo: Handle Success
			}
			else{
				//ToDo: Handle Failure, e.g. refresh token and resend request
			}
			return response;
		}
	}

	private static PPWebApiInterface sPPWebApiInterface;

	public static PPWebApiInterface getApi(String burl) {
		//ToDo: logging interceptor for third party?

		if (sPPWebApiInterface == null) {
//						if(devPrefs.getEnvironment()) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient client = new OkHttpClient.Builder()
//					.addNetworkInterceptor(NetworkInterceptor)
					.addInterceptor(loggingInterceptor)
					.build();

			Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
					.create();
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(burl)
					.addConverterFactory(GsonConverterFactory.create(gson))
					.client(client)
					.build();

			sPPWebApiInterface = retrofit.create(PPWebApiInterface.class);
		}
		return sPPWebApiInterface;
	}


	public interface PPWebApiInterface {
		@Headers({
				"Accept: application/json",
				"Content-Type: application/json"
		})

		// User API calls
		@GET("/user/v1/my/profile")
		Call<User> getUser(@Header("Authorization") String authorization);

		@GET("user/v1/my/profile/picture")
		Call<Image> downloadProfileImage(@Header("Authorization") String authorization);

		@GET("user/v1/my/profile/cover")
		Call<Image> downloadCoverImage(@Header("Authorization") String authorization);


		// Friends API calls
		@GET("user/v1/my/friends")
		Call<ArrayList<User>> getFriends(@Header("Authorization") String authorization);


		// Data / Buckets API calls
		@PUT("/app/v1/bucket")
		Call<Bucket> putData(@Body Bucket bucketconfig, @Header("Authorization") String authorization); // create/open bucket

		@GET("/app/v1/bucket")
		Call<JsonObject> readData(@QueryMap Map<String, String> queryparms, @Header("Authorization") String authorization);

		@POST("app/v1/bucket")
		Call<JsonObject> writeData(@Body JsonObject bodyparms, @Header("Authorization") String authorization);

		@GET("/user/v1/static/{id}")
		Call<Image> downloadImage(@Path("id") String imageId, @Header("Authorization") String authorization);


	}

	// Refresh / Access JWT mgt

	private static PPOauthInterface sPPOauthInterface;

	public static PPOauthInterface getOauthApi(@Nullable Interceptor NetworkInterceptor, String burl) {
		//ToDo: logging interceptor for third party?

		if (sPPOauthInterface == null) {
//			NetworkInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			OkHttpClient client = new OkHttpClient.Builder()
					.addNetworkInterceptor(NetworkInterceptor)
					.build();
			Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
					.create();
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(burl)
					.addConverterFactory(GsonConverterFactory.create(gson))
					.client(client)
					.build();

			sPPOauthInterface = retrofit.create(PPOauthInterface.class);
		}
		return sPPOauthInterface;
	}
	public interface PPOauthInterface {
		@Headers({
				"Accept: application/json",
				"Content-Type: application/json"
		})
		@POST("token")
		Call<Tokens> getTokens(@QueryMap Map<String, String> queryparms);
	}

	private Boolean refreshInProgress = false;

	public boolean refreshAccessToken() {

		synchronized (refreshInProgress) {
			if (refreshInProgress) return true;
			refreshInProgress = true;
		}
		_WebApi webApi = new _WebApi();

		Map<String, String> queryparms = new HashMap<String, String>()
		{{
			put("client_id", webApi.getDevPrefs().getClientId());
			put("client_secret", webApi.getDevPrefs().getClientSecret());
			put("refresh_token", webApi.getDevPrefs().getClientRefreshToken());
			put("grant_type", "refresh_token");
		}};

		Call<Tokens> call = getOauthApi(null, webApi.getDevPrefs().getBaseUrl()).getTokens(queryparms);
		call.enqueue(new Callback<Tokens>() {
			@Override
			public void onResponse(Call<Tokens> call, Response<Tokens> response) {
				if(response.code() == 200) {
					Tokens tokens = response.body();
					extractAndSaveTokens(tokens);
					Log.d("refresh token res: ", String.valueOf(response.body()));
					refreshInProgress = false;
				} else {
					Log.e("Error", "refreshingAccessToken");
					refreshInProgress = false;
				}
			}

			@Override
			public void onFailure(Call<Tokens> call, Throwable t) {
				Log.e("refresh token error:", "failed with " + t);
				refreshInProgress = false;
			}
		});
		return true;
	}

	public void extractAndSaveTokens(Tokens tokens) {
		Log.d("extractAndSaveTokens:", tokens.toString());
		String expires_in = tokens.getExpiresIn();
		ZonedDateTime date = ZonedDateTime.now();
		if (expires_in == "1d") {
			date.plusHours(12);
		} else {
			date.plusHours(1);
		}

		devPrefs.setAuthParms(tokens.getAccessToken(), tokens.getRefreshToken(), date.toString());

	}
}
