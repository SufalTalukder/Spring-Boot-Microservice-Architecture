package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class CartApiResponse<T> {

	private String status;
	private String message;
	private T content;
	private double totalPrice;

	// Without pagination data response
	public CartApiResponse(String status, String message, T content, double totalPrice) {
		this.status = status;
		this.message = message;
		this.content = content;
		this.totalPrice = totalPrice;
	}
}
