package com.sufaltalukder.DTOs;

import lombok.Data;

@Data
public class RequestAuthLoginDTO {

	private String authUserEmailAddress;
	private String authUserPassword;

}
