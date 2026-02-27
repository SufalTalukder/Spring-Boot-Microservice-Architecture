package com.sufaltalukder.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.NewsletterRequest;
import com.sufaltalukder.Mappers.NewsletterMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.NewsletterRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsletterMgmtServiceImpl implements NewsletterMgmtService {

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // Via feign client

	@Override
	public ApiResponse<NewsletterDTO> createNewsletterToggle(long authUserId,
			@Valid NewsletterRequest newsletterRequest) {

		long newsletterCount = newsletterRepository.existsNewsletterByUserId(newsletterRequest.getUserId());

		if (newsletterCount > 0) {
			return new ApiResponse<>("exist", "Newsletter already exists for this user.", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(newsletterRequest.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		NewsletterModel savingData = new NewsletterModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setUserInfo(user);
		savingData.setNewsletterToggle(newsletterRequest.getNewsletterToggle());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Newsletter added successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		NewsletterModel savedData = newsletterRepository.save(savingData);

		return new ApiResponse<NewsletterDTO>("success", "Newsletter added successfully.",
				NewsletterMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<NewsletterDTO> getNewsletterToggle(long authUserId, long newsletterId) {

		Optional<NewsletterModel> isNewsletterIdExist = newsletterRepository.findById(newsletterId);

		if (isNewsletterIdExist.isEmpty()) {
			return new ApiResponse<NewsletterDTO>("not found", "Newsletter not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Newsletter fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<NewsletterDTO>("success", "Newsletter fetched successfully.",
				NewsletterMapper.toDTO(isNewsletterIdExist.get()));
	}

	@Override
	public ApiResponse<List<NewsletterDTO>> getAllNewsletterToggle() {

		List<NewsletterModel> newsletters = newsletterRepository.findAllNewsletters();

		if (newsletters.isEmpty()) {
			return new ApiResponse<>("not found", "Newsletter(s) not found.", null);
		}

		List<NewsletterDTO> dtos = newsletters.stream().map(NewsletterMapper::toDTO).toList();

		return new ApiResponse<>("success", "All Newsletter(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<NewsletterDTO> updateNewsletterToggle(long newsletterId, long authUserId,
			@Valid NewsletterRequest newsletterRequest) {

		Optional<NewsletterModel> isNewsletterIdExist = newsletterRepository.findById(newsletterId);

		if (isNewsletterIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Newsletter not found.", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(newsletterRequest.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		NewsletterModel updatingData = isNewsletterIdExist.get();
		updatingData.setAuthUserInfo(authUser);
		updatingData.setUserInfo(user);
		updatingData.setNewsletterToggle(newsletterRequest.getNewsletterToggle());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PATCH);
		actionLogData.setActionLogMessage("Newsletter updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		NewsletterModel updatedData = newsletterRepository.save(updatingData);

		return new ApiResponse<>("success", "Newsletter updated successfully.", NewsletterMapper.toDTO(updatedData));
	}
}
