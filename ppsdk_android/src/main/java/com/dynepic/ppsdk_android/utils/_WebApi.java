package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dynepic.ppsdk_android.PPManager;
import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.models.Tokens;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._CallbackFunction;
import com.dynepic.ppsdk_android.utils._Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.OkHttp3Downloader;

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

import static java.lang.Thread.sleep;

//import okhttp3.Response;


public class _WebApi {
	private _DevPrefs devPrefs;
	private static Boolean refreshInProgress = false;

	public void setDevPrefs(_DevPrefs dp) { devPrefs = dp; }
	public _DevPrefs getDevPrefs() {
		return devPrefs;
	}

	public _WebApi(_DevPrefs devPrefs, _CallbackFunction._Auth cb, _CallbackFunction._Generic webInitCb) {
		this.setDevPrefs(devPrefs);
		setNotifyOnAuthChanges(cb);

		ZonedDateTime rightnow = ZonedDateTime.now();
		if(rightnow.isAfter(_Utils.dateTimeFromString(getDevPrefs().getTokenExpirationTime()))) {
			refreshAccessToken((Boolean status) -> {
				if (status) {
					Log.d("refreshAccessToken: status:", status.toString());
				}
				webInitCb.f(status);
			});
		} else {
			webInitCb.f(true);
		}
	}

	class PPCustomInterceptor implements Interceptor {
		@Override
		public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
			Request request = chain.request();
			okhttp3.Response response = chain.proceed(request);

			if(response.code() == 409 || response.code()/100 == 2){
				return response;
			} else {
				if(response.code() == 401) {
					refreshAccessToken((Boolean status) -> {
//						Log.d("Interceptor finished refreshing token", "cloning request");
//						Log.d("Interceptor retry request: ", request.toString());
						chain.call().clone(); // retry
					});
				}
				return response;
			}
		}
	}

	private static PPWebApiInterface sPPWebApiInterface;
	PPCustomInterceptor ppCustomInterceptor = new PPCustomInterceptor();

    public synchronized PPWebApiInterface getApi(String burl) {
		if (sPPWebApiInterface == null) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


			OkHttpClient client = new OkHttpClient.Builder()
					.addInterceptor(loggingInterceptor)
					.addInterceptor(ppCustomInterceptor)
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
		Call<JsonObject> putData(@Body Bucket bucketconfig, @Header("Authorization") String authorization); // creates/opens bucket

		@GET("/app/v1/bucket")
		Call<JsonObject> readData(@QueryMap Map<String, String> queryparms, @Header("Authorization") String authorization);

		@POST("app/v1/bucket")
		Call<JsonObject> writeData(@Body JsonObject bodyparms, @Header("Authorization") String authorization);

		@GET("/user/v1/static/{id}")
		Call<Image> downloadImage(@Path("id") String imageId, @Header("Authorization") String authorization);


	}


	private class BasicAuthInterceptor implements Interceptor {

		@Override
		public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
			final PPManager ppsdk = PPManager.getInstance();
			final Request original = chain.request();
			final Request.Builder requestBuilder = original.newBuilder()
					.header("Authorization", "Bearer " + getDevPrefs().getClientAccessToken());
			Request request = requestBuilder.build();
			return chain.proceed(requestBuilder.build());
		}
	}
	public OkHttp3Downloader createDownloader() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new BasicAuthInterceptor())
				.build();
		return new OkHttp3Downloader(okHttpClient);
	}




	// ------------------------------------------------------------------------------------------
	// Refresh / Access Token mgt
	// ------------------------------------------------------------------------------------------
	private static PPOauthInterface sPPOauthInterface;
	private _CallbackFunction._Auth authCallback;
	public void setNotifyOnAuthChanges(_CallbackFunction._Auth cb) {
		authCallback = cb;
	}

	public static PPOauthInterface getOauthApi(String burl) {
		if (sPPOauthInterface == null) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

			OkHttpClient client = new OkHttpClient.Builder()
//					.addNetworkInterceptor(loggingInterceptor)
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

			sPPOauthInterface = retrofit.create(PPOauthInterface.class);
		}

		return sPPOauthInterface;
	}

	public interface PPOauthInterface {
		@Headers({
				"Accept: application/json",
				"Content-Type: application/json"
		})
		@POST("oauth/token")
		Call<Tokens> getTokens(@QueryMap Map<String, String> queryparms);
	}

	public void refreshAccessToken(_CallbackFunction._Generic cb) {
		ZonedDateTime rightnow = ZonedDateTime.now();
		if(rightnow.isBefore(_Utils.dateTimeFromString(getDevPrefs().getTokenExpirationTime()))) {
			cb.f(true);
		}
		synchronized (refreshInProgress) {
			if (refreshInProgress) cb.f(true);
			refreshInProgress = true;
		}

		Log.d("refreshAccessToken AT:", getDevPrefs().getClientAccessToken());
		Map<String, String> queryparms = new HashMap<String, String>()
		{{
				put("client_id", getDevPrefs().getClientId());
				put("client_secret", getDevPrefs().getClientSecret());
				put("refresh_token", getDevPrefs().getClientRefreshToken());
				put("grant_type", "refresh_token");
			}};

		Call<Tokens> call = getOauthApi(getDevPrefs().getBaseUrl()).getTokens(queryparms);
		call.enqueue(new Callback<Tokens>() {

			@Override
			public void onResponse(Call<Tokens> call, Response<Tokens> response) {
				if(response.code() == 200) {
					Tokens tokens = response.body();
					extractAndSaveTokens(tokens);
					synchronized (refreshInProgress) { refreshInProgress = false; }
					cb.f(true);
				} else {
					Log.e("Error", "refreshingAccessToken");
					if(authCallback!= null) authCallback.f(false);
					synchronized (refreshInProgress) { refreshInProgress = false; }
					cb.f(false);
				}
			}

			@Override
			public void onFailure(Call<Tokens> call, Throwable t) {
				Log.e("refresh token error:", "failed with " + t);
				if(authCallback != null) authCallback.f(false);  // when refresh fails, auth is lost
				synchronized (refreshInProgress) { refreshInProgress = false; }
				cb.f(false);
			}
		});
    }

    public void extractAndSaveTokens(Tokens tokens) {
		ZonedDateTime date = ZonedDateTime.now();
		if (tokens.getExpiresIn() == "1d") {
			date.plusHours(12);
		} else {
			date.plusHours(1);
		}

		devPrefs.setAuthParms(tokens.getAccessToken(), tokens.getRefreshToken(), _Utils.stringFromDateTime(date));

		}
	}