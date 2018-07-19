package com.dynepic.ppsdk_android.models;
//package com.dynepic.ppsdk.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//-----------------------------------com.dynepic.ppsdk.auth.Tokens.java-----------------------------------


public class Tokens {

	@SerializedName("access_token")
	@Expose
	private String accessToken;
	@SerializedName("expires_in")
	@Expose
	private String expiresIn;
	@SerializedName("refresh_token")
	@Expose
	private String refreshToken;
	@SerializedName("token_type")
	@Expose
	private String tokenType;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

}