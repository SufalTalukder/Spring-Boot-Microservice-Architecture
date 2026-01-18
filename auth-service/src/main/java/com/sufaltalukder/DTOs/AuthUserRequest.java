package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserModel.AuthUserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserRequest {

	private long authUserId;
	private AuthUserModel actionByUserInfo;
	private String authUserName;
	private String authUserEmailAddress;
	private String authUserPassword;
	private String authUserPhoneNumber;
	private AuthUserActive authUserActive;
	private AuthUserType authUserType;
	private Instant authUserCreatedAt;
	private Instant authUserUpdatedAt;
}
