package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.NewsletterRequest;
import com.sufaltalukder.Models.ApiResponse;

import jakarta.validation.Valid;

public interface NewsletterMgmtService {

	ApiResponse<NewsletterDTO> createNewsletterToggle(long authUserId, @Valid NewsletterRequest newsletterRequest);

	ApiResponse<NewsletterDTO> getNewsletterToggle(long authUserId, long newsletterId);

	ApiResponse<List<NewsletterDTO>> getAllNewsletterToggle();

	ApiResponse<NewsletterDTO> updateNewsletterToggle(long newsletterId, long authUserId,
			@Valid NewsletterRequest newsletterRequest);

}
