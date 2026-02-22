package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.NewsletterModel.NewsletterToggle;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewsletterRequest {

	@NotNull(message = "User ID is required.")
	private long userId;

	@NotNull(message = "Newsletter needs to be select.")
	private NewsletterToggle newsletterToggle;
}
