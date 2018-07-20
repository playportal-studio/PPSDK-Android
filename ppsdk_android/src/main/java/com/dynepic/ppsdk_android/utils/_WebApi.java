package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.media.Image;

import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
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

	//

	private static PPWebApiInterface sPPWebApiInterface;

	public static PPWebApiInterface getApi(Interceptor NetworkInterceptor) {
		//PPManager ppsdk = new PPManager(CONTEXT);
		//ToDo: logging interceptor for third party?

		if (sPPWebApiInterface == null) {
//			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//			interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			OkHttpClient client = new OkHttpClient.Builder()
					.addNetworkInterceptor(NetworkInterceptor)
					.build();
			Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
					.create();
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl("https://sandbox.iokids.net")
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
		Call<Bucket> putData(@Body Bucket bucketconfig, @Header("Authorization") String authorization);

		@GET("/app/v1/bucket")
		Call<Bucket> readData(@QueryMap Map<String, String> queryparms, @Header("Authorization") String authorization);

		@POST("app/v1/bucket")
		Call<Bucket> writeData(@Body Map<String, String> bodyparms, @Header("Authorization") String authorization);

		@GET("/user/v1/static/{id}")
		Call<Image> downloadImage(@Path("id") String imageId, @Header("Authorization") String authorization);


	}
}