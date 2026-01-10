package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserModel.AuthUserType;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO {

	private Long authUserId;
	private AuthUserModel actionByUserInfo;
	private String authUserName;
	private String authUserEmailAddress;
	private String authUserPassword;
	private String authUserPhoneNumber;
	private String authUserImage;
	private AuthUserActive authUserActive;
	private AuthUserType authUserType;
	private Instant authUserCreatedAt;
	private Instant authUserUpdatedAt;
}
