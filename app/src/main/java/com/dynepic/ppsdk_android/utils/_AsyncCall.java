//package com.dynepic.ppsdk_android.utils;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.dynepic.ppsdk_android.models.User;
//
//import java.util.ArrayList;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static com.dynepic.ppsdk_android.utils._WebApi.getApi;
//
//
///**
// * This class handles asynchronous tasking
// * More is TBD
//
// ===================
// = Friends Request =
// ===================
//
// // Create request, delegate, execute request
//    _AsyncCall.getFriendsRequest friendsRequest = new _AsyncCall.getFriendsRequest(CONTEXT);
//    friendsRequest.delegate = this;
//    friendsRequest.execute((Void) null);
//
// // Create @Override methods to get results after the Async Call has been completed
//    @Override
//    public void onProcessFinish(ArrayList<User> RESPONSE){}
//
// */
//
//
//public class _AsyncCall {
//
//    public interface AsyncFriendsResponse {
//        void onFriendsResponse(ArrayList<String> output);
//    }
//
//    public static class getFriendsRequest extends AsyncTask<Void, Void, ArrayList<String>> {
//
//        private Context CONTEXT;
//        public AsyncFriendsResponse delegate;
//        private ArrayList<User> friendsList;
//        private ArrayList<String> allFriendsHandles;
//
//        public getFriendsRequest(Context context){
//            this.CONTEXT = context;
//        }
//
//        @Override
//        protected ArrayList<String> doInBackground(Void... params) {
//            //Netowkring
//            _DevPrefs devPrefs = new _DevPrefs(CONTEXT);
//            allFriendsHandles = new ArrayList<>();
//            Call<ArrayList<User>> friendsCall = getApi(CONTEXT).getFriends(devPrefs.getClientAccessToken());
//            friendsCall.enqueue(new Callback<ArrayList<User>>() {
//                @Override
//                public void onFriendsResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
//                    if (response.code() == 200) {
//                        System.out.println(response.body());
//                        friendsList = response.body();
//                        for (int i=0;i>=friendsList.size(); i++){
//                            allFriendsHandles.add(friendsList.get(i).getHandle());
//                        }
//
//                    }
//                    else{
//                        Log.e(" SSO_LOGIN_ERR","Error getting friends data.");
//                        Log.e(" SSO_LOGIN_ERR","Response code is : "+response.code());
//                        Log.e(" SSO_LOGIN_ERR","Response message is : "+response.message());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
//                    Log.e("SSO_LOGIN_ERR", "Request failed with throwable: " + t);
//                }
//            });
//            return allFriendsHandles;
//        }
//
//        @Override
//        protected void onPostExecute(final ArrayList<String> result) {
//            delegate.onFriendsResponse(result);
//        }
//
//        @Override
//        protected void onCancelled() {
//        }
//    }
//}
