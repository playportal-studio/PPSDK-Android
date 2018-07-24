package com.dynepic.ppsdk_android.utils;

import android.support.annotation.Nullable;

public interface _CallbackFunction {
	public void cbf(@Nullable String bucketName, @Nullable String key, @Nullable String data, @Nullable String error);
}

