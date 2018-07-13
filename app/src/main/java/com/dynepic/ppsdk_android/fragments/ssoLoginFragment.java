package com.dynepic.ppsdk_android.fragments;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dynepic.ppsdk.ppsdk_demoapp.R;
import com.dynepic.ppsdk_android.PPUserObject;
import com.dynepic.ppsdk_android.PPUserService;
import com.dynepic.ppsdk_android.utils._DevPrefs;
import com.dynepic.ppsdk_android.utils._DialogFragments;

import java.lang.reflect.Method;


//TODO: THIS NEEDS TO BE MADE PACKAGE PRIVATE
public class ssoLoginFragment extends DialogFragment {

    private Context CONTEXT;
    private Activity ACTIVITY_CONTEXT;
    private loadingFragment loadingFragment;
    private Intent NEXT_INTENT;

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
        View view = inflater.inflate(R.layout.basic_webview_layout, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ToDo: load up your interface here
        //EXAMPLE:
        loadingFragment = new loadingFragment(); //start loading icon, stopped within the webview "onPageFinished"
        _DialogFragments.showDialogFragmentNoClear(ACTIVITY_CONTEXT, loadingFragment, true);
        loadWebView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //ToDo: set your window size restrictions here
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
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
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        grabAccessToken(url, CONTEXT);
                        //view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        loadingFragment.dismiss();
                    }
                }
        );

        _DevPrefs devPrefs = new _DevPrefs(CONTEXT);
        String pre = "https://sandbox.iokids.net/oauth/signin?client_id=";
        String cid = devPrefs.getClientId();
        String mid = "&redirect_uri=";
        String uri = devPrefs.getClientRedirect();
        String post = "&state=beans&response_type=implicit";

        String url = pre + cid + mid + uri + post;
        webView.loadUrl(url);
    }


    public void grabAccessToken(String url, Context CONTEXT) {

        Uri uri = Uri.parse(url);
        String accessToken = uri.getQueryParameter("access_token");
        _DevPrefs devPrefs = new _DevPrefs(CONTEXT);
        devPrefs.setClientAccessToken(accessToken);

        System.out.println("URL = "+uri);

        PPUserObject obj = PPUserService.getUser(CONTEXT);
        System.out.println("USER OBJ = "+obj);

        if(!accessToken.equals("")){
            Log.i("ssoLoginFragment", "AccessToken : "+accessToken);
            try{
                CONTEXT.startActivity(NEXT_INTENT);
                ACTIVITY_CONTEXT.finish();
                this.dismiss();
            }catch (Exception e){
                e.printStackTrace();
                Log.e(" SSO_LOGIN_ERR","Did you specify an intent for your next activity?");
                Log.e(" SSO_LOGIN_ERR","Error in class: "+ACTIVITY_CONTEXT.getLocalClassName());
                Log.e(" SSO_LOGIN_ERR","Intent is: "+NEXT_INTENT);
            }
        }

    }

}