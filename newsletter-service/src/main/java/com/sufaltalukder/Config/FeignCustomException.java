package com.sufaltalukder.Config;

import com.sufaltalukder.Models.ApiResponse;

public class FeignCustomException extends RuntimeException {

	private final ApiResponse<?> apiResponse;
	private final int status;

	public FeignCustomException(ApiResponse<?> apiResponse, int status) {
		super(apiResponse.getMessage());
		this.apiResponse = apiResponse;
		this.status = status;
	}

	public ApiResponse<?> getApiResponse() {
		return apiResponse;
	}

	public int getStatus() {
		return status;
	}
}
