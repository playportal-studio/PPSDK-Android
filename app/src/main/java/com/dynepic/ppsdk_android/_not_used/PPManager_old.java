package com.dynepic.ppsdk_android._not_used;//package com.dynepic.ppsdk_android;
//
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//
//import com.dynepic.ppsdk_android.utils._DevPrefs;
//
//import java.time.ZonedDateTime;
//
//public class PPManager_old {
//
//	private Context CONTEXT;
//	private _DevPrefs devPrefs;
//
//	// SDK objectd
//	PPUserObject PPuserobj = new PPUserObject();
//    private PPUserService PPusersvc = new PPUserService();
//    private PPFriendsObject PPfriendsobj = new PPFriendsObject();
////    private PPDataService PPdatasvc= new PPDataService();
//
//    //Values
//	private String apiUrlBase =  "https://sandbox.iokids.net";
//	private String apiOauthBase = "https://sandbox.iokids.net/oauth/";
//	private String accessToken;
//	private String refreshToken;
//	private String clientId;
//	private ZonedDateTime expirationTime;
//
//	private Boolean refreshInProgress = false;
//
//	public PPManager_old(Context context){
//		CONTEXT = context;
//	}
//
//	public void configure(String id, String sec, String redir) {
//		Log.d("PPManager.Configure","Configuring PPManager:\nID : "+id+"\nSEC : "+sec+"\nREDIR : "+redir);
//
//		devPrefs.setClientId(id);
//		devPrefs.setClientSecret(sec);
//		devPrefs.setClientRedirect(redir);
//    }
//
//    public Boolean isConfigured(){
//		return devPrefs.exists();
//	}
//
//
//    // Public API calls for SDK
////	public void readBucket(String bucketName, String key, PPManager.CallbackFunction cb, Context CONTEXT) {
////		PPdatasvc.readBucket(bucketName, key, cb, CONTEXT);
////	}
////	public void writeBucket(String bucketName, String key, String value, Boolean push, PPManager.CallbackFunction cb, Context CONTEXT) {
////		PPdatasvc.writeBucket(bucketName, key, value, push, cb, CONTEXT);
////	}
////	public String getPrivateDataStorage() {
////		return PPuserobj.myUserObject.getMyDataStorage();
////	}
////	public String getPublicDataStorage() {
////		return PPuserobj.myUserObject.getMyGlobalDataStorage(CONTEXT);
////	}
////	PPUserObject getProfile() {
////		return PPusersvc.getProfile(false, CONTEXT);
////	}
//
////region OLD_DATA
//
////
////	public void setClientId(String clientId) {
////		this.clientId = clientId;
////	}
////
////	public void setClientSecret(String clientSecret) {
////		this.clientSecret = clientSecret;
////	}
////
////	public void setRedirectURI(String redirectURI) {
////		this.redirectURI = redirectURI;
////	}
////
////	String clientId;
////    String clientSecret;
////    String redirectURI;
////    ZonedDateTime expirationTime;
////    String auth_code;
////	boolean setImAnonymousStatus;
////
//
////
////	private PPManager() {  // A private Constructor prevents any other class from instantiating.
////		Log.d("PPManager:", "private constructor invoked");
////	}
////
////	private SharedPreferences devPrefs;
////
//
////	public interface PPOauthService {
////		@Headers({
////				"Accept: application/json",
////				"Content-Type: application/json"
////		})
////		@POST("token")
////		Call<Tokens> getTokens(@QueryMap Map<String, String> queryparms);
////	}
//
////
////	// Allow app to register a callback, which is invoked on user profile updates
////	UserListenerFunction userListenerFnx;
////
////	public interface UserListenerFunction { public void uf(PPUserObject u);  }
////
////	public void addUserListener(UserListenerFunction u) { Log.d("adding userListener: ", u.toString()); userListenerFnx = u; }
////
////    private void userListener(PPUserObject u) { if(userListenerFnx != null) userListenerFnx.uf(u); };
////
////endregion
//
////	public interface BucketCallbackFunction {
////		public boolean f(String bucketName, List<String> bucketUsers, boolean bucketIsPublic, String error);
////	}
////
////	public interface CallbackFunction {
////		public void fse(String bucketName, String key, String data, String error);
////	}
////
////	public interface UserProfileCallbackFunction {
////		public void f(ArrayList<User> friends);
////	}
//
////region OLD_CONFIG
////    public void configure(String id, String sec, String redir, Context context) {
////		Log.d("context:", context.toString());
////		devPrefs = context.getSharedPreferences("ppsdk-preferences", Context.MODE_PRIVATE);
////		androidContext = context;
////		getAuthPreferences();
////
////        clientId = id;
////        clientSecret = sec;
////        redirectURI = redir;
////        Log.d("PPSDK configure", id + " : " + sec + " : " + redir);
////
////        if(isAuthenticated()) {
////			getUserPreferences();
////            if(getProfileAndBucket()) {
////                userListener(PPuserobj);
////                return;
////            }
////        }
////        userListener(null);
////    }
////endregion
//
////    public boolean getProfileAndBucket() {
////		Log.d("getProfileAndBucket: ", "");
////        if(PPusersvc.getProfile(true, CONTEXT) != null) {
////			return true;
////		} else {
////        	return false;
////		}
////    }
//
//// ToDo: Since we aren't using getInitialToken(), this can be removed?
//    public static void handleOpenURL(String url, Context CONTEXT) {
//
//		String accessToken;
//
////		ppsdk.setImAnonymousStatus = false;
//
//        Uri uri = Uri.parse(url);
////        String protocol = uri.getScheme();
////        String server = uri.getAuthority();
////        String path = uri.getPath();
////        Set<String> args = uri.getQueryParameterNames();
////
////		Log.d("args: ", args.toString());
////
////		ppsdk.auth_code = uri.getQueryParameter("code");
//		accessToken = uri.getQueryParameter("access_token");
//		_DevPrefs sharedPrefs = new _DevPrefs(CONTEXT);
//		sharedPrefs.setClientAccessToken(accessToken);
//
//		System.out.println("URL = "+uri);
//
//		PPUserObject obj = PPUserService.getProfile(false, CONTEXT);
//		System.out.println("USER OBJ = "+obj);
//
//
//
////		ppsdk.refreshToken = uri.getQueryParameter("refresh_token");
////		String expires_in = uri.getQueryParameter("expires_in");
////
////		ZonedDateTime date = ZonedDateTime.now();
////		if (expires_in == "1d") {
////
////			date.plusHours(12);
////		} else {
////			date.plusHours(1);
////		}
////		ppsdk.expirationTime = date;
////
////		ppsdk.setAuthPreferences(); // save server tokens, etc.
////
////		if(ppsdk.getProfileAndBucket()) {
////			ppsdk.userListener(null);
////		} else {
////		}
//
//    }
//
//// ToDo: not used?
////    public boolean getInitialToken() {
////        Map<String, String> queryparms = new HashMap<String, String>();
////        queryparms.put("code", auth_code);
////        queryparms.put("redirect_uri",redirectURI);
////        queryparms.put("client_id",clientId);
////		queryparms.put("client_secret", clientSecret);
////		queryparms.put("grant_type", "implicit");
////        Log.d("getInitialToken parms: ", queryparms.toString());
////
////
////		Gson gson = new GsonBuilder()
////				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
////				.create();
////
////		Retrofit retrofit = new Retrofit.Builder()
////				.baseUrl(apiUrlBase)
////				.addConverterFactory(GsonConverterFactory.create(gson))
////				.build();
////
////		PPOauthService authService = retrofit.create(PPOauthService.class);
////		Call<Tokens> call = authService.getTokens(queryparms);
////		Log.d("queryparms: ", queryparms.toString());
////		call.enqueue(new Callback<Tokens>() {
////						 @Override
////						 public void onResponse(Call<Tokens> call, Response<Tokens> response) {
////							 int statusCode = response.code();
////							 Log.d("status: ", String.valueOf(statusCode));
////							 Tokens tokens = response.body();
////							 Log.d("getInitialToken res: ", String.valueOf(response.body()));
////						 }
////
////						 @Override
////						 public void onFailure(Call<Tokens> call, Throwable t) {
////							 Log.e("getInitialToken error:", "failed with " + t);
////						 }
////					 });
////		return true;
////	}
//
////ToDo: Not used?
////	public String getMyId() { return ""; }
////
////	public String getAccessToken() { return accessToken; }
//
////    public boolean refreshAccessToken() {
////		if ((refreshToken == null) || (refreshToken == "unknown")) {
////			Log.e("ERROR", "attempting to refresh token with null refreshToken:");
////			return false;
////		}
////
////		synchronized (refreshInProgress) {
////			if (refreshInProgress) return true;
////			refreshInProgress = true;
////		}
////
////		Map<String, String> queryparms = new HashMap<String, String>();
////		queryparms.put("client_id", devPrefs.getClientId());
////		queryparms.put("client_secret", devPrefs.getClientSecret());
////		queryparms.put("refresh_token", refreshToken);
////		queryparms.put("grant_type", "refresh_token");
////		Log.d("refreshAccessToken parms: ", queryparms.toString());
////
////		Gson gson = new GsonBuilder()
////				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
////				.create();
////
////		Retrofit retrofit = new Retrofit.Builder()
////				.baseUrl(apiOauthBase)
////				.addConverterFactory(GsonConverterFactory.create(gson))
////				.build();
////
////		PPOauthService authService = retrofit.create(PPOauthService.class);
////		Call<Tokens> call = authService.getTokens(queryparms);
////		Log.d("queryparms: ", queryparms.toString());
////		call.enqueue(new Callback<Tokens>() {
////			@Override
////			public void onResponse(Call<Tokens> call, Response<Tokens> response) {
////				int statusCode = response.code();
////				Log.d("status: ", String.valueOf(statusCode));
////				if (statusCode == 200) {
////					Tokens tokens = response.body();
////					extractAndSaveTokens(tokens);
////					Log.d("refreshAccessToken res: ", String.valueOf(response.body()));
////					refreshInProgress = false;
////				} else {
////					Log.e("Error", "refreshingAccessToken");
////					refreshInProgress = false;
////				}
////			}
////
////			@Override
////			public void onFailure(Call<Tokens> call, Throwable t) {
////				Log.e("refreshAccessToken error:", "failed with " + t);
////				refreshInProgress = false;
////			}
////		});
////
////		return true;
////
////    }
//
////    public void extractAndSaveTokens(Tokens tokens) {
////    	Log.d("extractAndSaveTokens:", tokens.toString());
////		accessToken = tokens.getAccessToken();
////		refreshToken = tokens.getRefreshToken();
////
////		String expires_in = tokens.getExpiresIn();
////		ZonedDateTime date = ZonedDateTime.now();
////		if (expires_in == "1d") {
////			date.plusHours(12);
////		} else {
////			date.plusHours(1);
////		}
////
////		expirationTime = date;
////		//ToDo: save tokens
////		//setAuthPreferences();
////    }
//
////    public boolean allTokensExist() {
////		Log.d("allTokensExist: at:", accessToken);
////		Log.d("allTokensExist: rt:", refreshToken);
////		if ((refreshToken != null) && (refreshToken != "unknown") && (accessToken != null) && (accessToken != "unknown")) {
////			Log.d("allTokensExist:", "true");
////			return true;
////		} else {
////			//ToDo: ========
////			//invalidateUserPreferences();
////			devPrefs.clear();
////			Log.d("allTokensExist:", "false");
////			return false;
////		}
////	}
//
////    public boolean tokensNotExpired() {
////    	ZonedDateTime currentDT = ZonedDateTime.now();
////		Log.d("current dateTime", currentDT.toString());
////		Log.d("Token expirationTime: ", expirationTime.toString());
////		if(currentDT.isBefore(expirationTime))  {
////			return TRUE;
////		} else {
////			Log.d("Token expirationTime: ", expirationTime.toString());
////			Log.d("present time: ", currentDT.toString());
////			return FALSE;
////		}
////    }
//
////	public void storeTokensInKeychain() {
////
////	}
////
////    public boolean isAuthenticated() {
////        if(allTokensExist()) {
////            if(tokensNotExpired()) {
////				if(clientId.equals(devPrefs.getClientId())) {
////					Log.d("isAuthenticated:", "true");
////					return true;
////				} else {
////					Log.d("isAuthenticated:", "false - wrong app id");
////					//invalidateUserPreferences();
////					devPrefs.clear();
////					return false;
////				}
////            } else {
////                if(refreshAccessToken()) {
////					if(clientId.equals(devPrefs.getClientId())) {
////						Log.d("isAuthenticated:", "true");
////						return true;
////					} else {
////						Log.d("isAuthenticated:", "false - wrong app id");
////						//invalidateUserPreferences();
////						devPrefs.clear();
////						return false;
////					}
////				} else {
////					Log.d("isAuthenticated:", "false");
////					//invalidateUserPreferences();
////					devPrefs.clear();
////					return false;
////                }
////            }
////        }
////		Log.d("isAuthenticated:", "false");
////		//invalidateUserPreferences();
////		devPrefs.clear();
////		return false;
////    }
//
////    public void logout() {
////		accessToken = null;
////		refreshToken = null;
////		devPrefs.clear();
////		//ToDo: =====
////		//setAuthPreferences();
////		//invalidateUserPreferences();
////    }
//
////	public void AnonymousLogin() { }
//
////	public String getPicassoParms() {
////		return apiUrlBase + "/user/v1/my/profile/picture";
////	}
//
////	private static class BasicAuthInterceptor implements Interceptor {
////
////		@Override
////		public okhttp3.Response intercept(Chain chain) throws IOException {
////			final PPManager ppsdk = PPManager.getInstance();
////			final Request original = chain.request();
////			final Request.Builder requestBuilder = original.newBuilder()
////					.header("Authorization", "Bearer " + ppsdk.accessToken);
////			Request request = requestBuilder.build();
////			return chain.proceed(requestBuilder.build());
////		}
////	}
//
////	public OkHttp3Downloader createDownloader() {
////
////// For logging - enable this section (adds a new Interceptor to OkHttp
////		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
////		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
////		OkHttpClient okHttpClient = new OkHttpClient.Builder()
////				.addInterceptor(new BasicAuthInterceptor())
////				.addInterceptor(logging)
//////				.readTimeout(30, TimeUnit.SECONDS)
//////				.writeTimeout(30, TimeUnit.SECONDS)
//////				.connectTimeout(30, TimeUnit.SECONDS)
////				.build();
////
////		return new OkHttp3Downloader(okHttpClient);
////	}
//
////	public ZonedDateTime dateTimeFromString(String datestring) {
////		try {
////			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
////			return ZonedDateTime.parse(datestring, formatter);
////		}
////		catch (DateTimeParseException exc) {
////			Log.e("%s is not parsable!%n", datestring);
////			throw exc;      // Rethrow the exception.
////		}
////    }
////
////    public String stringFromDateTime(ZonedDateTime dateTime) {
////		try {
////			DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
////			return dateTime.format(format);
////		}
////		catch (DateTimeException exc) {
////			System.out.printf("dateTime input can't be formatted!");
////			throw exc;
////		}
////    }
//
////	public void setImAnonymousStatus(boolean imAnonymous) {
////    }
////
////    public boolean getImAnonymousStatus() { return false;
////    }
//
//
////	private void getAuthPreferences() {
////		nvClientId = devPrefs.getString("clientId", "unknown");
////		accessToken = devPrefs.getString("accessToken", "unknown");
////		refreshToken = devPrefs.getString("refreshToken", "unknown");
////		String defaultDateTime = stringFromDateTime(ZonedDateTime.now().minusHours(1));
////		Log.d("defaultDateTime:", defaultDateTime);
////		expirationTime = dateTimeFromString(devPrefs.getString("expirationTime", defaultDateTime));
////
////		Log.d("getAuthPreferences id:", nvClientId);
////		Log.d("getAuthPreferences at:", accessToken );
////		Log.d("getAuthPreferences rt:", refreshToken );
////		Log.d("getAuthPreferences et:", expirationTime.toString());
////	}
//
////ToDo: This is handled in the configure() method?
////	private void setAuthPreferences() {
////		SharedPreferences.Editor editor = devPrefs.edit();
////		editor.putString("clientId", nvClientId);
////		editor.putString("accessToken", accessToken);
////		editor.putString("refreshToken", refreshToken);
////		editor.putString("expirationTime", stringFromDateTime(expirationTime));
////		editor.commit();
////
////		Log.d("setAuthPreferences id:", nvClientId);
////		Log.d("setAuthPreferences at:", accessToken != null? accessToken : "invalid accessToken");
////		Log.d("setAuthPreferences rt:", refreshToken  != null? accessToken : "invalid refreshToken");
////		Log.d("setAuthPreferences et:", expirationTime.toString());
////	}
//
////ToDo: This is handled by _SharedPrefs class?
////	private void getUserPreferences() {
////		String id = devPrefs.getString("newUserId", "unknown");
////		String handle = devPrefs.getString("handle", "unknown");
////		PPuserobj.initWithUserPreferences(id, handle);
////		Log.d("getUserPreferences userId:", PPuserobj.myUserObject.getUserId());
////		Log.d("getUserPreferences handle:", PPuserobj.myUserObject.getHandle());
////	}
//
////	public void invalidateUserPreferences() {
////		Log.d("invalidating UserPreferences :", "");
////		SharedPreferences.Editor editor = devPrefs.edit();
////		editor.putString("newUserId", "unknown");
////		editor.putString("handle", "unknown");
////		editor.commit();
////	}
//
////	public void setUserPreferences() {
////		SharedPreferences.Editor editor = devPrefs.edit();
////		String id = PPuserobj.myUserObject.getUserId();
////		String handle = PPuserobj.myUserObject.getHandle();
////		editor.putString("newUserId", id);
////		editor.putString("handle", handle);
////		editor.commit();
////
////		Log.d("setUserPreferences userId:", id);
////		Log.d("setUserPreferences handle:", handle);
////	}
//}