package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserModel.AuthUserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserRequest {

	private long authUserId;
	private AuthUserModel actionByUserInfo;

	@NotBlank(message = "Name is required")
	private String authUserName;

	@NotBlank(message = "Email address is required")
	@Email(message = "Invalid email format")
	private String authUserEmailAddress;

	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$", message = "Password must contain upper, lower, number and special character")
	private String authUserPassword;

	private String authUserPhoneNumber;
	private AuthUserActive authUserActive;
	private AuthUserType authUserType;
	private Instant authUserCreatedAt;
	private Instant authUserUpdatedAt;
}
