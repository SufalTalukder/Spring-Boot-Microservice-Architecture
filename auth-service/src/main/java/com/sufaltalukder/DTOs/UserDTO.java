package com.sufaltalukder.DTOs;

import java.sql.Date;
import java.time.ZonedDateTime;

import com.sufaltalukder.Models.UserModel.UserActive;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private long userId;
	private long authUserId;
	private String fullName;
	private String phoneNumber;
	private String emailAddress;
	private Date dob;
	private String userImage;
	private String userAddress;
	private String userReferralCode;
	private UserActive userActive;
	private ZonedDateTime userCreatedAt;
	private ZonedDateTime userUpdatedAt;

}
