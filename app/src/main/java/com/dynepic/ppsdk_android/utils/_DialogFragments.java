package com.dynepic.ppsdk_android.utils;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;

/**
 * This class handles dialog fragments (NOT TO BE CONFUSED WITH DIALOGS OR ALERT DIALOGS)
 * These are fragments that pop up on the screen like a dialog, but have a custom layout defined by you.

 =========
 = Usage =
 =========

 _DialogFragments.showDialogFragment(ACTIVITY_CONTEXT, new YOUR_FRAGMENT, BOOLEAN_CANCELABLE, FRAGMENT_TITLE_STRING);

 * The optional parameter 'FRAGMENT_TITLE_STRING' displays title text at the top center unless otherwise specified in the fragment.
 * the default value is 'dialog'. You can change the value in the fragment by using:

 getDialog().setTitle("YOUR_TITLE");

 *
 */

//ToDo: merge with _Fragments class?

public class _DialogFragments extends DialogFragment {

    //Same as below, default dialog title
    public static void showDialogFragment(Activity ACTIVITY_CONTEXT, DialogFragment dialogFragment, Boolean cancelable){
        showDialogFragment(ACTIVITY_CONTEXT, dialogFragment, cancelable, "dialog");
    }

    //Shows a new dialog fragment, clears any previous, has a title if it's not specified in the fragment.
    public static void showDialogFragment(Activity ACTIVITY_CONTEXT, DialogFragment dialogFragment, Boolean cancelable, String TITLE){
        FragmentTransaction transaction = ACTIVITY_CONTEXT.getFragmentManager().beginTransaction();
        _Fragments.clearPreviousFragments(ACTIVITY_CONTEXT);
        dialogFragment.setCancelable(cancelable);
        dialogFragment.show(transaction, TITLE);
    }

    //Shows a dialog fragment on top of everything else, without clearing base fragments
    public static void showDialogFragmentNoClear(Activity ACTIVITY_CONTEXT, DialogFragment dialogFragment, Boolean cancelable) {
        FragmentTransaction transaction = ACTIVITY_CONTEXT.getFragmentManager().beginTransaction();
        dialogFragment.setCancelable(cancelable);
        dialogFragment.show(transaction, "dialog");
    }

}