package com.sufaltalukder.DTOs;

import java.sql.Date;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel.UserActive;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	private long userId;
	private AuthUserModel authUserInfo;

	@NotBlank(message = "User name is required")
	private String fullName;

	@NotBlank(message = "Phone number is required")
	private String phoneNumber;

	@NotBlank(message = "Email address is required")
	private String emailAddress;

	private Date dob;

	@NotBlank(message = "Address is required")
	private String userAddress;

	private String userReferralCode;
	private UserActive userActive;

}
