package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.NewsletterModel.NewsletterToggle;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterDTO {

	private long newsletterId;
	private long userId;
	private long authUserId;
	private NewsletterToggle newsletterToggle;
	private ZonedDateTime newsletterCreatedAt;
	private ZonedDateTime newsletterUpdatedAt;
}
