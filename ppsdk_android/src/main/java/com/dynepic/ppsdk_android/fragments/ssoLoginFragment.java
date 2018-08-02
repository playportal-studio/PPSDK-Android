package ppsdk_android.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dynepic.ppsdk_android.PPManager;
import com.dynepic.ppsdk_android.R;
import com.dynepic.ppsdk_android.models.User;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;

import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ssoLoginFragment extends DialogFragment {

    private Context CONTEXT;
    private Activity ACTIVITY_CONTEXT;
    private loadingFragment loadingFragment;
    private Intent NEXT_INTENT;
    private _DevPrefs devPrefs;

    public ssoLoginFragment() {}

    public void setNextActivity(Intent intent){
        NEXT_INTENT = intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();
        ACTIVITY_CONTEXT = getActivity();
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.sso_login_view, container, false);
        Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingFragment = new loadingFragment();
        _DialogFragments.showDialogFragmentNoClear(ACTIVITY_CONTEXT, loadingFragment, true);
        loadWebView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(getDialog().getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }


    @SuppressLint("SetJavaScriptEnabled")//know where the app is going. non-issue.
    private void loadWebView(View view){
        WebView webView = view.findViewById(R.id.webViewWindow);
        assert webView != null;
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        try {
            //HTML5 requires DOM storage
            Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
            m1.invoke(webSettings, Boolean.TRUE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest WRR) {
                        String url = WRR.getUrl().toString();
                        grabTokenAndUser(url, CONTEXT);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        loadingFragment.dismiss();
                    }
                }
        );

        _DevPrefs devPrefs = new _DevPrefs(CONTEXT);
        String pre = devPrefs.getBaseUrl() + "/oauth/signin?client_id=";
        String cid = devPrefs.getClientId();
        String mid = "&redirect_uri=";
        String uri = devPrefs.getClientRedirect();
        String post = "&state=beans&response_type=implicit";

        String url = pre + cid + mid + uri + post;
        webView.loadUrl(url);
    }


    private void grabTokenAndUser(String url, Context CONTEXT) {

        Uri uri = Uri.parse(url);
        String accessToken = uri.getQueryParameter("access_token");
        devPrefs = new _DevPrefs(CONTEXT);
        devPrefs.setClientAccessToken(accessToken);
        devPrefs.setClientRefreshToken(uri.getQueryParameter("refresh_token"));

        ZonedDateTime date = ZonedDateTime.now();
        if(uri.getQueryParameter("expires_in") == "1d") {
            date.plusHours(12);
		} else {
			date.plusHours(1);
		}
        devPrefs.setTokenExpirationTime(date.toString());

        _DialogFragments.showDialogFragmentNoClear(ACTIVITY_CONTEXT, loadingFragment, false);
        PPManager ppManager = PPManager.getInstance();
        ppManager.updateUserFromWeb(this, loadingFragment, ACTIVITY_CONTEXT, NEXT_INTENT);
    }
}