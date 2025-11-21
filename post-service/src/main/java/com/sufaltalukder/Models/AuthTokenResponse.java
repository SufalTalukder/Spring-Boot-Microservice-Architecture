package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class AuthTokenResponse {

	private String authToken;

	public AuthTokenResponse(String authToken) {
		this.authToken = authToken;
	}
}
