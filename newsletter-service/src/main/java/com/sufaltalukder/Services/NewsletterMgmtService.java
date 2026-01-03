package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;

public interface NewsletterMgmtService {

	ApiResponse<NewsletterDTO> createNewsletterToggle(long authUserId, long userId, NewsletterModel newsletterModel);

	ApiResponse<NewsletterDTO> getNewsletterToggle(long authUserId, long newsletterId);

	ApiResponse<List<NewsletterDTO>> getAllNewsletterToggle();

	ApiResponse<NewsletterDTO> updateNewsletterToggle(long newsletterId, long authUserId, long userId,
			String newsletterToggle);

}
