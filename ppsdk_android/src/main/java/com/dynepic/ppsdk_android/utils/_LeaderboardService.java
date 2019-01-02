package com.dynepic.ppsdk_android.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dynepic.ppsdk_android.models.Leaderboard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Leaderboards support the following API calls:
//
// Get "a leaderboard"
// GET /leaderboard/v1
// REQUEST
// query: {
//   page?: Number,
//   limit?: Number,
//   categories?: Comma separated String list
// },
// headers: {
//   accesstoken: String
// }
//
// RESPONSE
// body: {
//   total: Number,
//   limit: Number,
//   page: Number,
//   pages: Number,
//   docs: [{
//     user: User,
//     score: String,
//     rank: Number,
//     categories: [String]
//   }]
// }

// Update "a Leaderboard"
// POST /leaderboard/v1
//
// REQUEST
// body: {
//   score: Number,
//   categories: [String]
// },
// headers: {
//   accesstoken: String
// }
//
// RESPONSE
// body: {
//   total: Number,
//   limit: Number,
//   page: Number,
//   pages: Number,
//   docs: [{
//     user: User,
//     score: String,
//     rank: Number,
//     categories: [String]
//   }]
// }

public class _LeaderboardService {
	private static final String TAG = "_LeaderboardService";
	public _WebApi webApi;

	public _LeaderboardService(_WebApi w) {
		Log.d("_LeaderboardService() ", "constructor");
		webApi = w;
	}


	public void get(Integer page, Integer limit, String categories, Context CONTEXT, _CallbackFunction._Leaderboard cb) {
		Log.d(TAG, "get: " + categories);
		Boolean readSuccess = true;
		_DevPrefs devPrefs = new _DevPrefs(CONTEXT);

		if (categories != null) {
			String btoken = "Bearer " + devPrefs.getClientAccessToken();

			Map<String, String> queryparms = new HashMap<>();
			queryparms.put("page", String.valueOf(page));
			queryparms.put("limit", String.valueOf(limit));
			queryparms.put("categories", categories);
			Call<Leaderboard> call = webApi.getApi(devPrefs.getBaseUrl()).getLeaderboard(queryparms, btoken);
			call.enqueue(new Callback<Leaderboard>() {
				@Override
				public void onResponse(Call<Leaderboard> call, Response<Leaderboard> response) {
					if (response.code() == 200) {
						Log.d(TAG, "attempt deserialization of response.body:" + String.valueOf(response.body()));
						Leaderboard leaderboardObject = response.body();
						cb.f(leaderboardObject, null);
					} else {
						Log.e(TAG, "get leaderboard error:" + response.message());
						cb.f(null, response.message());
					}
				}

				@Override
				public void onFailure(Call<Leaderboard> call, Throwable t) {
					Log.e(TAG, "get Leaderboard error, failed with:", t);
					cb.f(null, t.getMessage());
				}
			});
		}
	}

	public void update(Integer score, ArrayList<String> categories, Context CONTEXT, _CallbackFunction._Leaderboard cb) {
		Log.d(TAG, "update leaderboard" + "categories: " + categories + " score:" + score.toString());
		if (categories != null) {
			_DevPrefs devPrefs = new _DevPrefs(CONTEXT);
			String btoken = "Bearer " + devPrefs.getClientAccessToken();
			JsonArray ja = new JsonArray(categories.size());
			categories.forEach(cat -> ja.add(cat));

			JsonObject body = new JsonObject();
			body.addProperty("score", score);
			body.add("categories", ja);
			body.addProperty("access_token", btoken);


			Call<Leaderboard> call = webApi.getApi(devPrefs.getBaseUrl()).updateLeaderboard(body, btoken);
			Log.d(TAG, "update Leaderboard body: " + body.toString());
			call.enqueue(new Callback<Leaderboard>() {
				@Override
				public void onResponse(Call<Leaderboard> call, Response<Leaderboard> response) {
					if (response.code() == 200) {
						Leaderboard leaderboardObject = response.body();
						Log.d(TAG, "update leaderboard: " + String.valueOf(response.body()));
						cb.f(leaderboardObject, null);
					}
				}

				@Override
				public void onFailure(Call<Leaderboard> call, Throwable t) {
					Log.e(TAG, "update leaderboard error:" + "failed with " + t);
					cb.f(null, t.getMessage());
				}
			});

		} else {
			cb.f(null, "Invalid parms to update leaderboard write!");
		}
	}
}


