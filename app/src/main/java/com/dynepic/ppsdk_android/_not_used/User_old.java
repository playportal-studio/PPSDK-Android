package com.dynepic.ppsdk_android._not_used;//package com.dynepic.ppsdk_android.REFACTOR_IN_PROGRESS;
//
//import android.arch.core.BuildConfig;
//import android.util.Log;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//
//public class User_old {
//
//	@SerializedName("accountType")
//	@Expose
//	private String accountType;
//	@SerializedName("country")
//	@Expose
//	private String country;
//	@SerializedName("coverPhoto")
//	@Expose
//	private String coverPhoto;
//	@SerializedName("firstName")
//	@Expose
//	private String firstName;
//	@SerializedName("handle")
//	@Expose
//	private String handle;
//	@SerializedName("lastName")
//	@Expose
//	private String lastName;
//	@SerializedName("profilePic")
//	@Expose
//	private String profilePic;
//	@SerializedName("userId")
//	@Expose
//	private String userId;
//	@SerializedName("userType")
//	@Expose
//	private String userType;
//	@SerializedName("myDataStorage")
//	@Expose
//	private String myDataStorage;
//	@SerializedName("myGlobalDataStorage")
//	@Expose
//	private String myGlobalDataStorage;
//
//
//	public void populateUserData(Map<String, String> dictionary) {  // only needed on marshalling data from secure storage (automatically done on API call)
//		if (dictionary != null) {
//			Log.d("populateUserData User ", "");
//			for ( String key : dictionary.keySet() ) {
//				System.out.println( key );
//			}
//		}
//	}
//
//
//
//	public String getMyDataStorage() {
//		if(handle != null) {
//			return(handle + "@" + BuildConfig.APPLICATION_ID);//ppsdk.androidContext.getResources().getString(R.string.app_name);
//		} else {
//			return " ";
//		}
//	}
//
//	public String getMyGlobalDataStorage() {
//		if (handle != null) {
//			return("globalAppData" + "@" + BuildConfig.APPLICATION_ID);//+ ppsdk.androidContext.getResources().getString(R.string.app_name));
//		} else {
//			return "unknown";
//		}
//	}
////
////
//	public String getValueForKey(String key) throws IllegalAccessException {
//		for (Field field : this.getClass().getDeclaredFields()) {
//			field.setAccessible(true); // You might want to set modifier to public first.
//			try {
//				Object value = field.get(this);
//				if ((value != null) && (field.getName() == key)) {
//					return value.toString();
//				}
//			} catch (IllegalAccessException exc) {
//				throw exc;      // Rethrow the exception.
//			}
//		}
//		return null;
//	}
//
//
//}