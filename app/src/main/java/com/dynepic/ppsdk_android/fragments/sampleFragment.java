package com.dynepic.ppsdk_android.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dynepic.ppsdk_android.R;


//ToDo: delete this template class along with corresponding xml layout before final push to production

public class sampleFragment extends Fragment {

    public sampleFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.sample_layout, container, false);

        //ToDo: Load up your interface here.

        return view;
    }
}