package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserModel.AuthUserType;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserDTO {

	private long authUserId;
	private String authUserName;
	private String authUserEmailAddress;
	private String authUserPassword;
	private String authUserPhoneNumber;
	private String authUserImage;
	private long actionByUserId;
	private AuthUserActive authUserActive;
	private AuthUserType authUserType;
	private ZonedDateTime authUserCreatedAt;
	private ZonedDateTime authUserUpdatedAt;
}
