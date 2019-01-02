package com.dynepic.ppsdk_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Leader {
	//     user: User,
//     score: String,
//     rank: Number,
//     categories: [String]
//   }]
	@SerializedName("user")
	@Expose
	private com.dynepic.ppsdk_android.models.User user;


	@SerializedName("score")
	@Expose
	private String score;

	@SerializedName("rank")
	@Expose
	private String rank;

	@SerializedName("categories")
	@Expose
	private ArrayList<String> categories;


	public User getUser() {
		return user;
	}
	public String getRank() {
		return rank;
	}
	public String getScore() { return score; }
	public ArrayList<String> getCategories() {
		return categories;
	}
}