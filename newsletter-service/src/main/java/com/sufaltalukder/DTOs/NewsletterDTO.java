package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.NewsletterModel.NewsletterToggle;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterDTO {

	private long newsletterId;
	private AuthUserDTO authUserInfo;
	private UserDTO userInfo;
	private NewsletterToggle newsletterToggle;
	private Instant newsletterCreatedAt;
	private Instant newsletterUpdatedAt;

}