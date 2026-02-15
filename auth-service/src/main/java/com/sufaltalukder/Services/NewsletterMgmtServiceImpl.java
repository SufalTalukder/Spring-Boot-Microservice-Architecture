package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Mappers.NewsletterMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.NewsletterRepository;
import com.sufaltalukder.Repositories.UserRepository;

@Service
public class NewsletterMgmtServiceImpl implements NewsletterMgmtService {

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ApiResponse<NewsletterDTO> createNewsletterToggle(NewsletterModel newsletterModel) {
		Optional<UserModel> isUserIdExist = userRepository.findByUserId(newsletterModel.getUserId());

		if (isUserIdExist.isEmpty()) {
			return new ApiResponse<NewsletterDTO>("not found", "User not found.", null);
		}

		NewsletterModel savedData = newsletterRepository.save(newsletterModel);
		return new ApiResponse<NewsletterDTO>("success", "Newsletter added successfully.",
				NewsletterMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<NewsletterDTO> getNewsletterToggle(long newsletterId) {
		Optional<NewsletterModel> isNewsletterIdExist = newsletterRepository.findById(newsletterId);

		if (isNewsletterIdExist.isEmpty()) {
			return new ApiResponse<NewsletterDTO>("not found", "Newsletter not found.", null);
		}

		return new ApiResponse<NewsletterDTO>("success", "Newsletter fetched successfully.",
				NewsletterMapper.toDTO(isNewsletterIdExist.get()));
	}

	@Override
	public PaginationApiResponse<List<NewsletterDTO>> getAllNewsletterToggle(int pageNo, int pageSize) {
		Page<NewsletterModel> newsletters = newsletterRepository.findAll(PageRequest.of(pageNo - 1, pageSize));

		if (newsletters.isEmpty()) {
			return new PaginationApiResponse<>("not found", "Newsletter(s) not found.", null, 0, 0, 0);
		}

		List<NewsletterDTO> dtos = newsletters.stream().map(NewsletterMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Newsletters list fetched successfully.", dtos,
				newsletters.getNumber() + 1, newsletters.getSize(), newsletters.getTotalElements());
	}

	@Override
	public ApiResponse<NewsletterDTO> updateNewsletterToggle(long newsletterId, NewsletterModel newsletterModel) {
		NewsletterModel isNewsletterIdExist = newsletterRepository.findById(newsletterId).orElse(null);

		if (isNewsletterIdExist == null) {
			return new ApiResponse<>("not found", "Newsletter not found.", null);
		}

		UserModel isUserIdExist = userRepository.findByUserId(newsletterModel.getUserId()).orElse(null);

		if (isUserIdExist == null) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		isNewsletterIdExist.setUserId(newsletterModel.getUserId());
		isNewsletterIdExist.setNewsletterToggle(newsletterModel.getNewsletterToggle());
		isNewsletterIdExist.setNewsletterUpdatedAt(ZonedDateTime.now());

		NewsletterModel updatedData = newsletterRepository.save(isNewsletterIdExist);

		return new ApiResponse<>("success", "Newsletter updated successfully.", NewsletterMapper.toDTO(updatedData));
	}

}
