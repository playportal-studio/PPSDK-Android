package com.dynepic.ppsdk_android.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dynepic.ppsdk_android.R;

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
        View view = inflater.inflate(R.layout.sample_empty_layout, container, false);


        //ToDo: load up your interface here


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