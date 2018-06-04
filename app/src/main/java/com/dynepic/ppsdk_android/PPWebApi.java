package com.dynepic.ppsdk_android;

import android.media.Image;

import com.dynepic.ppsdk_android.models.Bucket;
import com.dynepic.ppsdk_android.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

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

public class PPWebApi {


	private static PPWebApiInterface sPPWebApiInterface;

	public static PPWebApiInterface getApi() {
		PPManager ppsdk = PPManager.getInstance();

		if (sPPWebApiInterface == null) {
			Gson gson = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
					.create();
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(ppsdk.apiUrlBase)
					.addConverterFactory(GsonConverterFactory.create(gson))
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

		// Data / Buckets API calls
		@PUT("/app/v1/bucket")
		Call<Bucket> putData(@Body Map<String, String> bodyparms, @Header("Authorization") String authorization);

		@GET("/app/v1/bucket")
		Call<Bucket> readData(@QueryMap Map<String, String> queryparms, @Header("Authorization") String authorization);

		@POST("app/v1/bucket")
		Call<Bucket> writeData(@Body Map<String, String> bodyparms, @Header("Authorization") String authorization);

		@GET("/user/v1/static/{id}")
		Call<Image> downloadImage(@Path("id") String imageId, @Header("Authorization") String authorization);


	}
}