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

import com.dynepic.ppsdk.ppsdk_demoapp.R;
import com.dynepic.ppsdk_android.utils._DialogFragments;

import java.lang.reflect.Method;

//ToDo: delete this template class along with corresponding xml layout before final push to production

public class sampleDialogFragment extends DialogFragment {

    private Context CONTEXT;
    private Activity ACTIVITY_CONTEXT;

    public sampleDialogFragment() {
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
//        loadingFragment = new loadingFragment(); //start loading icon, stopped within the webview "onPageFinished"
//        _DialogFragments.showDialogFragmentNoClear(ACTIVITY_CONTEXT, loadingFragment, true);
//        loadWebView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //ToDo: set your window size restrictions here
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

}