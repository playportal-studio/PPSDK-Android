package ppsdk_android.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.dynepic.ppsdk_android.R;


/**
 *
 * This is a simple "loading spinner dialog" fragment.
 * You can customize your loading icon here.
 * Show it using the '_DialogFragments' class method 'showDialogFragmentNoClear'.
 * See that class for details.
 *
 */

public class loadingFragment extends DialogFragment {

    private Context CONTEXT;

    public loadingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONTEXT = getActivity();
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

}