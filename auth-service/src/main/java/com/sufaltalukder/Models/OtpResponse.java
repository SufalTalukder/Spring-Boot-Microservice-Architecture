package com.sufaltalukder.Models;

import lombok.Data;

@Data
public class OtpResponse {

	private String otp;

	public OtpResponse(String otp) {
		this.otp = otp;
	}
}
