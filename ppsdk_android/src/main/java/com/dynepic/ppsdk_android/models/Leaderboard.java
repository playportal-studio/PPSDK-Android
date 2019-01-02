package com.dynepic.ppsdk_android.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Leaderboard {

	@SerializedName("total")
	@Expose
	private Number total;

	@SerializedName("limit")
	@Expose
	private Number limit;

	@SerializedName("page")
	@Expose
	private Number page;

	@SerializedName("pages")
	@Expose
	private Number pages;

	@SerializedName("docs")
	@Expose
	private JsonArray docs;  // contains leaders

	public ArrayList<Leader> getLeaders() {
		// Convert Json array to ArrayList
		if(docs != null) {
			ArrayList<Leader> leaders = new ArrayList<>();
			Gson gson = new Gson();

			for(int i=0; i<docs.size(); i++) {
				JsonElement je = docs.get(i);
				Log.d("Leaderboard extract je:", String.valueOf(je));
				leaders.add( gson.fromJson(je, Leader.class) );
			}
			return leaders;
		}
		return null;
	}

	public Number getTotal() {
		return total;
	}
	public Number getLimit() { return limit; }
	public Number getPage() { return page; }
	public Number getPages() { return pages; }

}