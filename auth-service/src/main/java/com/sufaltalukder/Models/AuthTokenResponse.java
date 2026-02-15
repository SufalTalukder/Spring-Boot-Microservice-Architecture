package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class AuthTokenResponse {

	private String accessToken;
	private String refreshToken;

	public AuthTokenResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
