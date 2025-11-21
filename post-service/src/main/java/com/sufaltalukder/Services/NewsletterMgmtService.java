package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.PaginationApiResponse;

public interface NewsletterMgmtService {

	ApiResponse<NewsletterDTO> createNewsletterToggle(NewsletterModel newsletterModel);

	ApiResponse<NewsletterDTO> getNewsletterToggle(long newsletterId);

	PaginationApiResponse<List<NewsletterDTO>> getAllNewsletterToggle(int pageNo, int pageSize);

	ApiResponse<NewsletterDTO> updateNewsletterToggle(long newsletterId, NewsletterModel newsletterModel);

}
