package com.dynepic.ppsdk_android.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.dynepic.ppsdk_android.models.User;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface _CallbackFunction {
	public interface _Data {
		//		public void f(String bucketName, @Nullable String key, @Nullable String data, @Nullable String error);
		public void f(String bucketName, @Nullable String key, @Nullable JsonObject data, @Nullable String error);
	}

	public interface _Friends {
		public void f(@Nullable ArrayList<User> friends, @Nullable String error);
	}
}


