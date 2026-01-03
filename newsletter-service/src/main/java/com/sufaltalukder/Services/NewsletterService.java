package com.sufaltalukder.Services;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;

public interface NewsletterService {

	ApiResponse<NewsletterDTO> getNewsletterToggle(long userId);

	ApiResponse<NewsletterDTO> updateNewsletterToggle(long userId, NewsletterModel newsletterModel);

}
