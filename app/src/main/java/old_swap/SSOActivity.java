//package com.dynepic.ppsdk_android;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.webkit.WebResourceError;
//import android.webkit.WebResourceRequest;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
////public class SSOActivity extends AppCompatActivity {
//public class SSOActivity extends Activity {
//
//	public SSOActivity()
//	{}
//
//
//	private WebView mWebview ;
//	private ProgressDialog pd;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		mWebview  = new WebView(this);
//		mWebview.getSettings().setJavaScriptEnabled(true);
//		pd = ProgressDialog.show(this, "", "...loading",true);
//		final Activity activity = this;
//
//		mWebview.setWebViewClient(new WebViewClient() {
//			@SuppressWarnings("deprecation")
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				//This method will be executed each time a page finished loading.
//				//The only we do is dismiss the progressDialog, in case we are showing any.
//				if(pd != null && pd.isShowing()){
//					Log.d("Closing:", "mWebview in onPageFinished");
//					pd.dismiss();
//				}
//			}
//
//			@Override//ToDo: Does not override methods
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				Log.d("SSO url redirect: ", url);
//				PPManager.handleOpenURL(url);
//
//				//ToDo: fragments
//				// User is logged in, send to MainActivity
//				//Intent restartMainActivity = new Intent(SSOActivity.this, activityClass);
//				//SSOActivity.this.startActivity(restartMainActivity);
//				//finishActivity(1);
//				return true; // return true => webview won't load the url;
//			}
//
//			protected void onPostExecute(Boolean status){
//			}
//
//			@Override
//			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//				Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
//			}
//			@TargetApi(android.os.Build.VERSION_CODES.M)
//			@Override
//			public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//				// Redirect to deprecated method, so you can use it in all SDK versions
//				onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//			}
//		});
//		PPManager ppsdk = PPManager.getInstance();
//		String pre = "https://sandbox.iokids.net/oauth/signin?client_id=";
//		String cid = ppsdk.clientId;
//		String mid = "&redirect_uri=";
//		String uri = ppsdk.redirectURI;
//		String post = "&state=beans&response_type=implicit";
//
//		String url = pre + cid + mid + uri + post;
//
//		mWebview .loadUrl(url);
//		setContentView(mWebview );
//	}
//
//
//}
//
//
