package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.models.Tokens;
import com.dynepic.ppsdk_android.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
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

public class _WebApi {
	private static _DevPrefs devPrefs;
	private static Context CONTEXT;
	public void setContext(Context c) { CONTEXT = c; }
	public _DevPrefs getDevPrefs() {
		if (devPrefs == null) devPrefs = new _DevPrefs(CONTEXT);
		return devPrefs;
	}

	private static PPWebApiInterface sPPWebApiInterface;

	public static PPWebApiInterface getApi(@Nullable Interceptor NetworkInterceptor, String burl) {
		//ToDo: logging interceptor for third party?

		if (sPPWebApiInterface == null) {
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
		Call<Bucket> readData(@QueryMap Map<String, String> queryparms, @Header("Authorization") String authorization);

		@POST("app/v1/bucket")
		Call<Bucket> writeData(@Body Map<String, String> bodyparms, @Header("Authorization") String authorization);

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
//					Tokens tokens = response.body();
					tokens = extractAndSaveTokens(tokens, response.body());
					Log.d("refreshAccessToken res: ", String.valueOf(response.body()));
					refreshInProgress = false;
					return true;
				} else {
					Log.e("Error", "refreshingAccessToken");
					refreshInProgress = false;
					return false;
				}
			}

			@Override
			public void onFailure(Call<Tokens> call, Throwable t) {
				Log.e("refreshAccessToken error:", "failed with " + t);
				refreshInProgress = false;
				return false;
			}
		});



    }

    public void extractAndSaveTokens(Okhttp3.ResponseBody responseBody) {
    	Log.d("extractAndSaveTokens:", responseBody);
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
}