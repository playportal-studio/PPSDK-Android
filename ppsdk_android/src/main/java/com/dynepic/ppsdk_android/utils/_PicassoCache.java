package com.dynepic.ppsdk_android.utils;

import android.content.Context;

import com.dynepic.ppsdk_android.PPManager;
import com.squareup.picasso.Picasso;

public class _PicassoCache {

    /**
     * Static Picasso Instance
     */
    private static Picasso picassoInstance = null;

    /**
     * PicassoCache Constructor
     *
     * @param context application Context
     */
    private _PicassoCache (Context context) {
        Picasso.Builder builder = new Picasso.Builder(context);
        picassoInstance = builder.downloader(PPManager.getInstance().imageDownloader()).build();
    }

    /**
     * Get Singleton Picasso Instance
     *
     * @param context application Context
     * @return Picasso instance
     */
    public static Picasso getPicassoInstance (Context context) {

        if (picassoInstance == null) {

            new _PicassoCache(context);
            return picassoInstance;
        }

        return picassoInstance;
    }

}
