package com.dynepic.ppsdk_android._not_used;//package com.dynepic.ppsdk_android.models;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.dynepic.ppsdk_android.utils._DevPrefs;
//import com.dynepic.ppsdk_android.utils._UserPrefs;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.dynepic.ppsdk_android.utils.PPWebApi.getApi;
//
//public class PPUserObject {
//
////	public String userId;
////	public String handle;
////	public String firstName;
////	public String lastName;
////	public String country;
////	public String accountType;
////	public String userType;
////	public Map<String, String> parentFlags;
////	public String profilePicId;
////	public String coverPhotoId;
////	public String myDataStorage;
////	public String myAppGlobalDataStorage;
////	public String myAge;
////	public boolean isAnonymousUser;
////	Map<String, String> myUserObjectMap;
//
//    public User myUserObject;
//
////    public Map<String, String>getMyStoredProfile() {
////		Map<String, String> t = new HashMap<>();
////		t.putAll(myUserObjectMap);
////		return t;
////	}
////
////	public void populateUserData(Map<String, String> map) {
////		myUserObjectMap.putAll(map);
////	}
////
////	public void initWithUserPreferences(String id, String handle)
////	{
////		if(myUserObject == null) myUserObject = new User();
////		myUserObject.setUserId(id);
////		myUserObject.setHandle(handle);
////	}
//
//    public void populateUserData(User user, Context CONTEXT) throws IllegalAccessException {
//        Log.d("inflateWithUser:", user.toString());
//        myUserObject = user;
//        _UserPrefs userPrefs = new _UserPrefs(CONTEXT);
//        userPrefs.setUserName(myUserObject.getValueForKey("firstName"));
//        userPrefs.setUserDetails1(myUserObject.getValueForKey("lastName") + " is " + myUserObject.getValueForKey("handle"));
//    }
//
//    public String getValueForKey(String key) {
//        Log.d("getValueForKey:", "key: " + key);
//        try {
//            return myUserObject.getValueForKey(key);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
////	public void inflateFriendsListFromArray(ArrayList<User> users) throws IllegalAccessException {
////		Log.d("inflating friends list...", "");
////		for (User u: users) {
////			Log.d("friend:", u.getHandle());
////		}
////	}
//
//    //	public HashMap<String, String> userDictionary;
////	public Image copyOfProfileImage;
////	public Image copyOfCoverImage;
//
//
//    public static PPUserObject getUser(Context CONTEXT) {
//        final PPUserObject userObject = new PPUserObject();
//
//        _DevPrefs settings = new _DevPrefs(CONTEXT);
//
//
//        String btoken = "Bearer " + settings.getClientAccessToken();
//
//        Call<User> call = getApi(CONTEXT).getUser(btoken);
//        Log.d("getProfile: ", "no parms");
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                int statusCode = response.code();
//                Log.d("status: ", String.valueOf(statusCode));
//                if (statusCode == 200) {
//                    User user = response.body();
//                    Log.d("getProfile: user:", user.getHandle());
//                    Log.d("getProfile res: ", String.valueOf(response.body()));
//                    Log.d("username:", user.getHandle());
//                    Log.d("my data:", user.getMyDataStorage());
//                    Log.d("global data:", user.getMyGlobalDataStorage());
//                    Log.d("userObj: ", user.getUserId());
//
//                    try {
//                        userObject.populateUserData(user, CONTEXT);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    //ToDo:  store user data
//                    //ppsdk.setUserPreferences(); // stash into nvram
//
////					try {
////						ppsdk.PPuserobj.populateUserData(user);
////					} catch (IllegalAccessException e) {
////						e.printStackTrace();
////					}
//
//
////					if (openBuckets) {
////						ArrayList<String> bucketUsers = new ArrayList<>();
////						ArrayList<String> emptyBucketUsers = new ArrayList<>();
////						bucketUsers.add(user.getUserId());
////
////						ppsdk.PPdatasvc.createBucket(user.getMyDataStorage(), bucketUsers, false, (String bucketName, String key, String data, String error) -> {
////							if (error == null) {
////								Log.d("getProfileAndBucket:", "opened user data bucket");
////								ppsdk.PPdatasvc.createBucket(user.getMyGlobalDataStorage(), emptyBucketUsers, true, (String bucketName2, String key2, String data2, String error2) -> {
////									if (error2 == null) {
////										Log.d("getProfileAndBucket:", "opened global data bucket");
////									}
////								});
////							}
////						});
////					}
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e("getProfile error:", "failed with " + t);
//            }
//
//        });
//        return userObject;
//    }
//
//
//////
//////	public void getFriendsProfiles(PPManager.UserProfileCallbackFunction cb)
//////	{
//////		PPManager ppsdk = new PPManager();
//////		String btoken = "Bearer " + accessToken;
//////		Call<ArrayList<User>> call = getApi().getFriends(btoken);
//////		Log.d("getFriends: ", "no parms");
//////
//////		call.enqueue(new Callback<ArrayList<User>>() {
//////			@Override
//////			public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//////				int statusCode = response.code();
//////
//////				Log.d("status: ", String.valueOf(statusCode));
//////				if (statusCode == 200) {
//////					ArrayList<User> friends = response.body();
//////					Log.d("getFriends: user:", friends.toString());
//////					try {
//////						Log.d("calling inflate friends from array:", "");
//////						ppsdk.PPuserobj.inflateFriendsListFromArray(friends);
//////					} catch (IllegalAccessException e) {
//////						e.printStackTrace();
//////					}
//////					Log.d("getFriends response: ", String.valueOf(response.body()));
//////					cb.f(friends);
//////				} else {
//////					cb.f(null);
//////				}
//////			}
//////
//////			@Override
//////			public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//////				Log.e("getFriendsProfiles error:", "failed with " + t);
//////				Log.e("getFriendsProfiles call:",  call.toString());
//////			}
//////		});
//////	}
////
////
////
////
////
////
////	public String getMyId() {
////		if (userDictionary != null) {
////			return userDictionary.get("userId");
////		} else {
////			return "unknown";
////		}
////	}
////
////	public String getMyUsername() {
////		if (userDictionary != null) {
////			return userDictionary.get("handle");
////		} else {
////			return "unknown";
////		}
////	}
//
//}