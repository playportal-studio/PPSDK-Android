package com.dynepic.ppsdk_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;


class BucketData {
	@SerializedName("Iteration")
	@Expose
	private Integer iteration;
	public Integer getIteration() {
		return iteration;
	}
	public void setIteration(Integer iteration) {
		this.iteration = iteration;
	}

}

public class Bucket {
	@SerializedName("createdDate")
	@Expose
	private String createdDate;

	@SerializedName("data")
	@Expose
	private HashMap<String, String> data=null;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("public")
	@Expose
	private Boolean _public;

	@SerializedName("users")
	@Expose
	private List<String> users = null;

	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

    public HashMap<String, String> getData() {		return data;	}
	public void setData(HashMap<String, String> data) {		this.data = data;	}

	public String getId() {		return id;	}
	public void setId(String id) {		this.id = id;	}
	public Boolean getPublic() {		return _public;	}
	public void setPublic(Boolean _public) {		this._public = _public;	}
	public List<String> getUsers() {		return users;	}
	public void setUsers(List<String> users) {		this.users = users;	}

}