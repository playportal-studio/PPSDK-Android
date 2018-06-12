package com.dynepic.ppsdk_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.dynepic.ppsdk_android.models.Tokens;
import com.dynepic.ppsdk_android.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class PPManager {

    // Public API calls for SDK
	public void readBucket(String bucketName, String key, PPManager.CallbackFunction cb) {
		PPdatasvc.readBucket(bucketName, key, cb);
	}
	public void writeBucket(String bucketName, String key, String value, Boolean push, PPManager.CallbackFunction cb) {
		PPdatasvc.writeBucket(bucketName, key, value, push, cb);
	}
	public String getPrivateDataStorage() {
		return PPuserobj.myUserObject.getMyDataStorage();
	}
	public String getPublicDataStorage() {
		return PPuserobj.myUserObject.getMyGlobalDataStorage();
	}
	PPUserObject getProfile() {
		return PPusersvc.getProfile(false);
	}


	public Context androidContext;  // context of parent app for this SDK


	private static Boolean refreshInProgress = false;


    String apiUrlBase =  "https://sandbox.iokids.net";
	String apiOauthBase = "https://sandbox.iokids.net/oauth/";
	String accessToken;
    String refreshToken;
	String nvClientId;
	String clientId;
    String clientSecret;
    String redirectURI;
    ZonedDateTime expirationTime;
    String auth_code;
	boolean setImAnonymousStatus;

	// SDK objectd
    PPUserObject PPuserobj = new PPUserObject();
    PPUserService PPusersvc = new PPUserService();
    PPFriendsObject PPfriendsobj = new PPFriendsObject();
    PPDataService PPdatasvc= new PPDataService();

	// PPManager is a singleton
	private static PPManager ppManager = new PPManager();
	public static PPManager getInstance( ) { return ppManager; }
	private PPManager() {  // A private Constructor prevents any other class from instantiating.
		Log.d("PPManager:", "private constructor invoked");
	}

	private SharedPreferences sharedPrefs;

	public interface PPOauthService {
		@Headers({
				"Accept: application/json",
				"Content-Type: application/json"
		})
		@POST("token")
		Call<Tokens> getTokens(@QueryMap Map<String, String> queryparms);
	}

	// Allow app to register a callback, which is invoked on user profile updates
	UserListenerFunction userListenerFnx;
	public interface UserListenerFunction { public void uf(PPUserObject u);  }
	public void addUserListener(UserListenerFunction u) { Log.d("adding userListener: ", u.toString()); userListenerFnx = u; }
    private void userListener(PPUserObject u) { if(userListenerFnx != null) userListenerFnx.uf(u); };

	public interface BucketCallbackFunction {
		public boolean f(String bucketName, List<String> bucketUsers, boolean bucketIsPublic, String error);
	}
	public interface CallbackFunction {
		public void fse(String bucketName, String key, String data, String error);
	}

	public interface UserProfileCallbackFunction {
		public void f(ArrayList<User> friends);
	}

    public void configure(String id, String sec, String redir, Context context)
    {
		Log.d("context:", context.toString());
		sharedPrefs = context.getSharedPreferences("ppsdk-preferences", Context.MODE_PRIVATE);
		androidContext = context;
		getAuthPreferences();

        clientId = id;
        clientSecret = sec;
        redirectURI = redir;
        Log.d("PPSDK configure", id + " : " + sec + " : " + redir);

        if(isAuthenticated()) {
			getUserPreferences();
            if(getProfileAndBucket()) {
                userListener(PPuserobj);
                return;
            }
        }
        userListener(null);
    }

    public boolean getProfileAndBucket() {
		Log.d("getProfileAndBucket: ", "");
        if(PPusersvc.getProfile(true) != null) {
			return true;
		} else {
        	return false;
		}
    }


    public static void handleOpenURL(String url)
    {
    	PPManager ppsdk = PPManager.getInstance();

    	ppsdk.setImAnonymousStatus = false;

        Uri uri = Uri.parse(url);
        String protocol = uri.getScheme();
        String server = uri.getAuthority();
        String path = uri.getPath();
        Set<String> args = uri.getQueryParameterNames();

		Log.d("args: ", args.toString());

		ppsdk.auth_code = uri.getQueryParameter("code");
		ppsdk.accessToken = uri.getQueryParameter("access_token");
		ppsdk.refreshToken = uri.getQueryParameter("refresh_token");
		String expires_in = uri.getQueryParameter("expires_in");

		ZonedDateTime date = ZonedDateTime.now();
		if (expires_in == "1d") {

			date.plusHours(12);
		} else {
			date.plusHours(1);
		}
		ppsdk.expirationTime = date;

		ppsdk.setAuthPreferences(); // save server tokens, etc.

		if(ppsdk.getProfileAndBucket()) {
			ppsdk.userListener(null);
		} else {
		}

    }

    public boolean getInitialToken()
    {
        Map<String, String> queryparms = new HashMap<String, String>();
        queryparms.put("code", auth_code);
        queryparms.put("redirect_uri",redirectURI);
        queryparms.put("client_id",clientId);
		queryparms.put("client_secret", clientSecret);
		queryparms.put("grant_type", "implicit");
        Log.d("getInitialToken parms: ", queryparms.toString());


		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.create();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(apiUrlBase)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		PPOauthService authService = retrofit.create(PPOauthService.class);
		Call<Tokens> call = authService.getTokens(queryparms);
		Log.d("queryparms: ", queryparms.toString());
		call.enqueue(new Callback<Tokens>() {
						 @Override
						 public void onResponse(Call<Tokens> call, Response<Tokens> response) {
							 int statusCode = response.code();
							 Log.d("status: ", String.valueOf(statusCode));
							 Tokens tokens = response.body();
							 Log.d("getInitialToken res: ", String.valueOf(response.body()));
						 }

						 @Override
						 public void onFailure(Call<Tokens> call, Throwable t) {
							 Log.e("getInitialToken error:", "failed with " + t);
						 }
					 });
		return true;
	}

	public String getMyId()
	{
		return "";
	}

	public String getAccessToken()
	{
		return accessToken;
	}

    public boolean refreshAccessToken() {
		if ((refreshToken == null) || (refreshToken == "unknown")) {
			Log.e("ERROR", "attempting to refresh token with null refreshToken:");
			return false;
		}

		synchronized (refreshInProgress) {
			if (refreshInProgress) return true;
			refreshInProgress = true;
		}

		Map<String, String> queryparms = new HashMap<String, String>();
		queryparms.put("client_id", clientId);
		queryparms.put("client_secret", clientSecret);
		queryparms.put("refresh_token", refreshToken);
		queryparms.put("grant_type", "refresh_token");
		Log.d("refreshAccessToken parms: ", queryparms.toString());

		Gson gson = new GsonBuilder()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
				.create();

		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(apiOauthBase)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		PPOauthService authService = retrofit.create(PPOauthService.class);
		Call<Tokens> call = authService.getTokens(queryparms);
		Log.d("queryparms: ", queryparms.toString());
		call.enqueue(new Callback<Tokens>() {
			@Override
			public void onResponse(Call<Tokens> call, Response<Tokens> response) {
				int statusCode = response.code();
				Log.d("status: ", String.valueOf(statusCode));
				if (statusCode == 200) {
					Tokens tokens = response.body();
					extractAndSaveTokens(tokens);
					Log.d("refreshAccessToken res: ", String.valueOf(response.body()));
					refreshInProgress = false;
				} else {
					Log.e("Error", "refreshingAccessToken");
					refreshInProgress = false;
				}
			}

			@Override
			public void onFailure(Call<Tokens> call, Throwable t) {
				Log.e("refreshAccessToken error:", "failed with " + t);
				refreshInProgress = false;
			}
		});

		return true;

    }

    public void extractAndSaveTokens(Tokens tokens)
    {
    	Log.d("extractAndSaveTokens:", tokens.toString());
		accessToken = tokens.getAccessToken();
		refreshToken = tokens.getRefreshToken();

		String expires_in = tokens.getExpiresIn();
		ZonedDateTime date = ZonedDateTime.now();
		if (expires_in == "1d") {
			date.plusHours(12);
		} else {
			date.plusHours(1);
		}
		expirationTime = date;
		nvClientId = clientId;
		setAuthPreferences();
    }

    public boolean allTokensExist() {
		Log.d("allTokensExist: at:", accessToken);
		Log.d("allTokensExist: rt:", refreshToken);
		if ((refreshToken != null) && (refreshToken != "unknown") && (accessToken != null) && (accessToken != "unknown")) {
			Log.d("allTokensExist:", "true");
			return true;
		} else {
			invalidateUserPreferences();
			Log.d("allTokensExist:", "false");
			return false;
		}
	}

    public boolean tokensNotExpired()
    {
    	ZonedDateTime currentDT = ZonedDateTime.now();
		Log.d("current dateTime", currentDT.toString());
		Log.d("Token expirationTime: ", expirationTime.toString());
		if(currentDT.isBefore(expirationTime))  {
			return TRUE;
		} else {
			Log.d("Token expirationTime: ", expirationTime.toString());
			Log.d("present time: ", currentDT.toString());
			return FALSE;
		}
    }

	public void storeTokensInKeychain()
	{

	}

    public boolean isAuthenticated()
    {
        if(allTokensExist()) {
            if(tokensNotExpired()) {
				if(clientId.equals(nvClientId)) {
					Log.d("isAuthenticated:", "true");
					return true;
				} else {
					Log.d("isAuthenticated:", "false - wrong app id");
					invalidateUserPreferences();
					return false;
				}
            } else {
                if(refreshAccessToken()) {
					if(clientId.equals(nvClientId)) {
						Log.d("isAuthenticated:", "true");
						return true;
					} else {
						Log.d("isAuthenticated:", "false - wrong app id");
						invalidateUserPreferences();
						return false;
					}
				} else {
					Log.d("isAuthenticated:", "false");
					invalidateUserPreferences();
					return false;
                }
            }
        }
		Log.d("isAuthenticated:", "false");
		invalidateUserPreferences();
		return false;
    }

    public void logout()
    {
		accessToken = null;
		refreshToken = null;
		setAuthPreferences();
		invalidateUserPreferences();
    }

	public void AnonymousLogin() {
	}

	public String getPicassoParms()
	{
		return apiUrlBase + "/user/v1/my/profile/picture";
	}

	private static class BasicAuthInterceptor implements Interceptor {

		@Override
		public okhttp3.Response intercept(Chain chain) throws IOException {
			final PPManager ppsdk = PPManager.getInstance();
			final Request original = chain.request();
			final Request.Builder requestBuilder = original.newBuilder()
					.header("Authorization", "Bearer " + ppsdk.accessToken);
			Request request = requestBuilder.build();
			return chain.proceed(requestBuilder.build());
		}
	}
	public OkHttp3Downloader createDownloader() {

// For logging - enable this section (adds a new Interceptor to OkHttp
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.addInterceptor(new BasicAuthInterceptor())
				.addInterceptor(logging)
//				.readTimeout(30, TimeUnit.SECONDS)
//				.writeTimeout(30, TimeUnit.SECONDS)
//				.connectTimeout(30, TimeUnit.SECONDS)
				.build();

		return new OkHttp3Downloader(okHttpClient);
	}



	public ZonedDateTime dateTimeFromString(String datestring)
    {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
			return ZonedDateTime.parse(datestring, formatter);
		}
		catch (DateTimeParseException exc) {
			Log.e("%s is not parsable!%n", datestring);
			throw exc;      // Rethrow the exception.
		}
    }

    public String stringFromDateTime(ZonedDateTime dateTime)
    {
		try {
			DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
			return dateTime.format(format);
		}
		catch (DateTimeException exc) {
			System.out.printf("dateTime input can't be formatted!");
			throw exc;
		}
    }

	public void setImAnonymousStatus(boolean imAnonymous)
    {
    }

    public boolean getImAnonymousStatus()
    {
        return false;
    }



	private void getAuthPreferences() {
		nvClientId = sharedPrefs.getString("clientId", "unknown");
		accessToken = sharedPrefs.getString("accessToken", "unknown");
		refreshToken = sharedPrefs.getString("refreshToken", "unknown");
		String defaultDateTime = stringFromDateTime(ZonedDateTime.now().minusHours(1));
		Log.d("defaultDateTime:", defaultDateTime);
		expirationTime = dateTimeFromString(sharedPrefs.getString("expirationTime", defaultDateTime));

		Log.d("getAuthPreferences id:", nvClientId);
		Log.d("getAuthPreferences at:", accessToken );
		Log.d("getAuthPreferences rt:", refreshToken );
		Log.d("getAuthPreferences et:", expirationTime.toString());
	}

	private void setAuthPreferences() {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString("clientId", nvClientId);
		editor.putString("accessToken", accessToken);
		editor.putString("refreshToken", refreshToken);
		editor.putString("expirationTime", stringFromDateTime(expirationTime));
		editor.commit();

		Log.d("setAuthPreferences id:", nvClientId);
		Log.d("setAuthPreferences at:", accessToken != null? accessToken : "invalid accessToken");
		Log.d("setAuthPreferences rt:", refreshToken  != null? accessToken : "invalid refreshToken");
		Log.d("setAuthPreferences et:", expirationTime.toString());
	}
	private void getUserPreferences() {
		String id = sharedPrefs.getString("newUserId", "unknown");
		String handle = sharedPrefs.getString("handle", "unknown");
		PPuserobj.initWithUserPreferences(id, handle);
		Log.d("getUserPreferences userId:", PPuserobj.myUserObject.getUserId());
		Log.d("getUserPreferences handle:", PPuserobj.myUserObject.getHandle());
	}

	public void invalidateUserPreferences() {
		Log.d("invalidating UserPreferences :", "");
		SharedPreferences.Editor editor = sharedPrefs.edit();
		editor.putString("newUserId", "unknown");
		editor.putString("handle", "unknown");
		editor.commit();
	}

	public void setUserPreferences() {
		SharedPreferences.Editor editor = sharedPrefs.edit();
		String id = PPuserobj.myUserObject.getUserId();
		String handle = PPuserobj.myUserObject.getHandle();
		editor.putString("newUserId", id);
		editor.putString("handle", handle);
		editor.commit();

		Log.d("setUserPreferences userId:", id);
		Log.d("setUserPreferences handle:", handle);
	}
}