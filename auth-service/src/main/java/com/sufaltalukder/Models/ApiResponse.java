package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class ApiResponse<T> {
	private String status;
	private String message;
	private T content;

	// Without pagination data response
	public ApiResponse(String status, String message, T content) {
		this.status = status;
		this.message = message;
		this.content = content;
	}
}