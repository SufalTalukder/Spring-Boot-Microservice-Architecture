package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterDTO {

	private long newsletterId;
	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private String newsletterToggle;
	private ZonedDateTime newsletterCreatedAt;
	private ZonedDateTime newsletterUpdatedAt;
}