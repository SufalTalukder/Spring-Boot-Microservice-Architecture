package com.sufaltalukder.DTOs;

import java.sql.Date;

import com.sufaltalukder.Models.UserModel.UserActive;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

	private long userId;
	private String fullName;
	private String phoneNumber;
	private String emailAddress;
	private Date dob;
	private String userImage;
	private String userAddress;
	private String userReferralCode;
	private UserActive userActive;

}
