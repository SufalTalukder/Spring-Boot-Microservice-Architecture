package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserModel.AuthUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

	private long authUserId;
	private String authUserName;
	private String authUserEmailAddress;
	private String authUserPhoneNumber;
	private String authUserImage;
	private AuthUserActive authUserActive;
	private AuthUserType authUserType;

}
