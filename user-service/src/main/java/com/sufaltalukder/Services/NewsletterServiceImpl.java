package com.sufaltalukder.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Mappers.NewsletterMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Repositories.NewsletterRepository;

@Service
public class NewsletterServiceImpl implements NewsletterService {

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Override
	public ApiResponse<NewsletterDTO> getNewsletterToggle(long userId) {
		NewsletterModel existingNewsletter = newsletterRepository.findByUserId(userId);

		if (existingNewsletter == null) {
			return new ApiResponse<>("not found", "User not exist in newsletter record.", null);
		}
		if (existingNewsletter.getUserId() != userId) {
			return new ApiResponse<>("not same", "User not same as newsletter user ID.", null);
		}

		return new ApiResponse<>("success", "User newsletter fetched successfully.",
				NewsletterMapper.toDTO(existingNewsletter));
	}

	@Override
	public ApiResponse<NewsletterDTO> updateNewsletterToggle(NewsletterModel newsletterModel) {
		NewsletterModel existingNewsletter = newsletterRepository.findByUserId(newsletterModel.getUserId());

		NewsletterModel savedNewsletter;

		if (existingNewsletter == null) {
			NewsletterModel newSubscriber = new NewsletterModel();
			newSubscriber.setUserId(newsletterModel.getUserId());
			newSubscriber.setNewsletterToggle(newsletterModel.getNewsletterToggle());
			savedNewsletter = newsletterRepository.save(newSubscriber);

			return new ApiResponse<>("success", "Newsletter subscribed successfully.",
					NewsletterMapper.toDTO(savedNewsletter));
		} else {
			existingNewsletter.setNewsletterToggle(newsletterModel.getNewsletterToggle());
			savedNewsletter = newsletterRepository.save(existingNewsletter);

			return new ApiResponse<>("success", "Newsletter subscription updated successfully.",
					NewsletterMapper.toDTO(savedNewsletter));
		}
	}
}
