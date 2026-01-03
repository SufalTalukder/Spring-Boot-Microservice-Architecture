package com.sufaltalukder.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Mappers.NewsletterMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.NewsletterRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.UserFeignService;

@Service
public class NewsletterServiceImpl implements NewsletterService {

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserFeignService userFeignService; // feign service

	@Override
	public ApiResponse<NewsletterDTO> getNewsletterToggle(long userId) {

		// calling micro-service via feign client
		ApiResponse<NewsletterDTO> response = userFeignService.getNewsletterSubscribed(userId);

		return new ApiResponse<>("success", "Newsletter fetched successfully.", response.getContent());
	}

	@Override
	public ApiResponse<NewsletterDTO> updateNewsletterToggle(long userId, NewsletterModel newsletterModel) {

		NewsletterModel existingNewsletter = newsletterRepository.findById(userId).orElse(null);

		NewsletterModel savedNewsletter;

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		if (existingNewsletter == null) {
			NewsletterModel newSubscriber = new NewsletterModel();
			newSubscriber.setUserInfo(user);
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
