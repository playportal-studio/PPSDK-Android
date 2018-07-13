package com.dynepic.ppsdk_android.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.IntDef;

import com.dynepic.ppsdk.ppsdk_demoapp.R;


/**
 * This class is for controlling fragments and fragment transitions.
 * Pulled from Github(github.com/preslm) and modified here for particular usage.
 * These methods only handle fragments that have existing containers within the current activity.

 =========
 = Usage =
 =========
 This will transition a fragment into a container already defined by you, handling the fragment transaction,
 and any fragment animations. More animations are TBD. No animation (or default pop-in) is allowed.
 Tags are an option. If none is given, it uses the fragment name as the tag.

 Put the following code in your controlling class:

 _Fragments.toFragment(ACTIVITY_CONTEXT, new YOUR_FRAGMENT(), R.id.FRAGMENT_CONTAINER);
 (OR)
 _Fragments.toFragment(ACTIVITY_CONTEXT, new YOUR_FRAGMENT(), R.id.FRAGMENT_CONTAINER, FRAGMENT_TRANSITION_STYLE);

 */

public class _Fragments extends Fragment {

    @IntDef({SLIDE_FROM_RIGHT,
            SLIDE_FROM_LEFT,
            SLIDE_FROM_TOP,
            SLIDE_FROM_BOTTOM,
            CLOSE, OPEN,
            FADE, NONE})

    @interface Style {}

    public static final int
            SLIDE_FROM_RIGHT =0,
            SLIDE_FROM_LEFT =1,
            SLIDE_FROM_TOP =2,
            SLIDE_FROM_BOTTOM =3,
            CLOSE=4, OPEN=5,
            FADE=6, NONE=7;

    //Clears all fragments before adding in the next one.
    public static void clearPreviousFragments(Activity ACTIVITY_CONTEXT){
        ACTIVITY_CONTEXT.getFragmentManager();
        ACTIVITY_CONTEXT.getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void toFragment(Activity ACTIVITY_CONTEXT, Fragment FRAGMENT, int FRAGMENT_ID){
        String TAG = FRAGMENT.toString();
        toFragment(ACTIVITY_CONTEXT, FRAGMENT, FRAGMENT_ID, _Fragments.NONE, TAG);
    }

    public static void toFragment(Activity ACTIVITY_CONTEXT, Fragment FRAGMENT, int FRAGMENT_ID, String TAG){
        toFragment(ACTIVITY_CONTEXT, FRAGMENT, FRAGMENT_ID, _Fragments.NONE, TAG);
    }

    public static void toFragment(Activity ACTIVITY_CONTEXT, Fragment FRAGMENT, int FRAGMENT_ID, @Style int STYLE){
        String TAG = FRAGMENT.toString();
        toFragment(ACTIVITY_CONTEXT, FRAGMENT, FRAGMENT_ID, STYLE, TAG);
    }

    public static void toFragment(Activity ACTIVITY_CONTEXT, Fragment FRAGMENT, int FRAGMENT_ID, @Style int STYLE, String TAG){
        FragmentTransaction transaction = ACTIVITY_CONTEXT.getFragmentManager().beginTransaction();
        switch (STYLE) {
            case 0:
                transaction.setCustomAnimations(R.animator.slide_right_enter,
                        R.animator.slide_right_exit,
                        R.animator.slide_right_back_enter,
                        R.animator.slide_right_back_exit);
                break;
            case 1:
                transaction.setCustomAnimations(R.animator.slide_left_enter,
                        R.animator.slide_left_exit,
                        R.animator.slide_left_back_enter,
                        R.animator.slide_left_back_exit);
                break;
            case 2:
                transaction.setCustomAnimations(R.animator.slide_up_enter,
                        R.animator.slide_up_exit,
                        R.animator.slide_up_back_enter,
                        R.animator.slide_up_back_exit);
                break;
            case 3:
                transaction.setCustomAnimations(R.animator.slide_down_enter,
                        R.animator.slide_down_exit,
                        R.animator.slide_down_back_enter,
                        R.animator.slide_down_back_exit);
                break;
            case 4:
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                break;
            case 5:
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                break;
            case 6:
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                break;
            default:
                break;
        }

        transaction.add(FRAGMENT_ID, FRAGMENT);
        transaction.addToBackStack(TAG);
        transaction.commit();
    }
}