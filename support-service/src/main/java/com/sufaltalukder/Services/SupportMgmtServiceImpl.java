package com.sufaltalukder.Services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.RequestSupportDTO;
import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Mappers.SupportMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.SupportModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.SupportRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class SupportMgmtServiceImpl implements SupportMgmtService {

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SupportRepository supportRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<SupportDTO> addUserSupport(long authUserId, long userId, RequestSupportDTO requestSupportDTO) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		SupportModel savingData = new SupportModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setUserInfo(user);
		savingData.setSupportText(requestSupportDTO.getSupportText());
		savingData.setSupportStatus(requestSupportDTO.getSupportStatus());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("User support created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User support created successfully.",
				SupportMapper.toDTO(supportRepository.save(savingData)));
	}

	@Override
	public ApiResponse<SupportDTO> getUserSupportDetails(long authUserId, long userId, long supportId) {

		SupportModel support = supportRepository.findBySupportIdAndUserInfo_UserId(supportId, userId).orElse(null);

		if (support == null) {
			return new ApiResponse<>("not found", "Support not found for this user.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("User support fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User support fetched successfully.", SupportMapper.toDTO(support));
	}

	@Override
	public ApiResponse<List<SupportDTO>> getAllUserSupports(String supportStatus) {

		SupportModel.SupportStatus statusEnum = null;

		if (supportStatus != null && !supportStatus.isBlank()) {
			try {
				statusEnum = SupportModel.SupportStatus.valueOf(supportStatus.toUpperCase());
			} catch (IllegalArgumentException e) {
				return new ApiResponse<>("error", "Invalid support status.", null);
			}
		}

		List<SupportModel> supports = supportRepository.findAllUserSupportsBySupportStatus(statusEnum);

		if (supports.isEmpty()) {
			return new ApiResponse<>("not found", "No supports found.", null);
		}

		return new ApiResponse<>("success", "User supports fetched successfully.",
				supports.stream().map(SupportMapper::toDTO).toList());
	}

	@Override
	public ApiResponse<SupportDTO> updateUserSupportDetails(long authUserId, long supportId, long userId,
			RequestSupportDTO requestSupportDTO) {

		SupportModel support = supportRepository.findBySupportIdAndUserInfo_UserId(supportId, userId).orElse(null);

		if (support == null) {
			return new ApiResponse<>("not found", "Support not found for this user.", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		support.setAuthUserInfo(authUser);
		support.setUserInfo(user);
		support.setSupportText(requestSupportDTO.getSupportText());
		support.setSupportStatus(requestSupportDTO.getSupportStatus());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("User support updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User support updated successfully.",
				SupportMapper.toDTO(supportRepository.save(support)));
	}

	@Override
	public ApiResponse<SupportDTO> deleteUserSupportDetails(long authUserId, long userId, long supportId) {

		SupportModel support = supportRepository.findBySupportIdAndUserInfo_UserId(supportId, userId).orElse(null);

		if (support == null) {
			return new ApiResponse<>("not found", "Support not found for this user.", null);
		}

		supportRepository.delete(support);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("User support deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User support deleted successfully.", null);
	}
}
