package com.dynepic.ppsdk_android.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dynepic.ppsdk_android.PPManager;
import com.dynepic.ppsdk_android.R;
import com.dynepic.ppsdk_android.utils._DialogFragments;

import java.lang.reflect.Method;


public class ssoLoginFragment extends DialogFragment {

    private Context CONTEXT;
    private Activity ACTIVITY_CONTEXT;
    private loadingFragment loadingFragment;
    private String redirectURI, clientId;

    public ssoLoginFragment() {
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
                    @SuppressWarnings("deprecation")//IT'S NOT DEPRECATED.
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //ToDo: test this method
                        PPManager.handleOpenURL(url);
                        //view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        //Stop your loading here
                        loadingFragment.dismiss();
                    }
                }
        );

        //ToDo: Are we sandboxing all users?
        String pre = "https://sandbox.iokids.net/oauth/signin?client_id=";
        String cid = clientId;
        String mid = "&redirect_uri=";
        String uri = redirectURI;
        String post = "&state=beans&response_type=implicit";

        String url = pre + cid + mid + uri + post;

        webView.loadUrl(url);
    }

    public void setLoginCredentials(String Client_ID, String Redirect_URL){
        this.clientId = Client_ID;
        this.redirectURI = Redirect_URL;
    }

}