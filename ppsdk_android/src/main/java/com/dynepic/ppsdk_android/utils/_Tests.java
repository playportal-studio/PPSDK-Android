package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.util.Log;

import java.time.ZonedDateTime;

public class _Tests {

	public void _Tests() {
			Log.d("_Tests ", "constructor");
	}

	public void expireAccessToken(Context context) {
		_DevPrefs devPrefs = new _DevPrefs(context);
		ZonedDateTime date = ZonedDateTime.now();
		date.minusMinutes(1);
		devPrefs.setTokenExpirationTime(date.toString());
	}

	public void invalidateAccessToken(Context context) {
		Log.d(context.getPackageName(), "_Tests() invalidateAccessToken");
		_DevPrefs devPrefs = new _DevPrefs(context);
		devPrefs.setClientAccessToken("");
	}

	public void invalidateRefreshToken(Context context) {
		_DevPrefs devPrefs = new _DevPrefs(context);
		devPrefs.setClientRefreshToken("");
	}
}
