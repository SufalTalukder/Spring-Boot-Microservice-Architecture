package com.sufaltalukder.DTOs;

import java.sql.Date;
import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel.UserActive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	private long userId;
	private AuthUserModel authUserInfo;
	private String fullName;
	private String phoneNumber;
	private String emailAddress;
	private Date dob;
	private String userAddress;
	private String userReferralCode;
	private UserActive userActive;
	private Instant userCreatedAt;
	private Instant userUpdatedAt;

}
