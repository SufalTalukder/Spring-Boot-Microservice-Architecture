package com.sufaltalukder.Services;

import java.io.IOException;
import java.nio.file.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.Mappers.AuthUserMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Utils.AuthJwtUtil;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class AuthUserMgmtServiceImpl implements AuthUserMgmtService {

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Autowired
	private AuthJwtUtil authJwtUtil;

	private final String UPLOAD_DIR = "uploads";

	private final String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=]).{8,}$";

	@Override
	public ApiResponse<AuthTokenResponse> loginAuthUser(String authUserEmailAddress, String authUserPassword) {
		AuthUserModel user = authUserRepository.findByAuthUserEmailAddress(authUserEmailAddress);

		if (user == null) {
			return new ApiResponse<>("not found", "Provided email doesn't exist.", null);
		}

		String encodedProvidedPassword = Base64.getEncoder().encodeToString(authUserPassword.getBytes());
		if (!encodedProvidedPassword.equals(user.getAuthUserPassword())) {
			return new ApiResponse<>("not matched", "Provided email or password doesn't match.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setAuthUserId(user.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Login successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		// Generate token with email and user id as claims
		String authToken = authJwtUtil.generateToken(authUserEmailAddress, user.getAuthUserId());
		return new ApiResponse<>("success", "Login successfully.", new AuthTokenResponse(authToken));
	}

	@Override
	public ApiResponse<AuthUserDTO> createAuthUser(long authUserId, AuthUserModel authUserInfo) {
		String rawPassword = authUserInfo.getAuthUserPassword();

		if (rawPassword == null || rawPassword.isEmpty()) {
			return new ApiResponse<>("required", "Password is required.", null);
		}
		if (rawPassword.length() < 8) {
			return new ApiResponse<>("weak password", "Password minimum 8 characters needed.", null);
		}
		if (!Pattern.matches(passwordRegex, rawPassword)) {
			return new ApiResponse<>("invalid password", "Password does not meet the strength requirements.", null);
		}

		// Base64 encode the password before storing
		String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes());
		authUserInfo.setAuthUserPassword(encodedPassword);
		authUserInfo.setActionByUserId(1);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Auth user created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		AuthUserModel savedAuthUserInfo = authUserRepository.save(authUserInfo);
		return new ApiResponse<>("success", "Auth user created successfully.", AuthUserMapper.toDTO(savedAuthUserInfo));
	}

	@Override
	public ApiResponse<String> uploadImage(long authUserId, MultipartFile file) {
		Optional<AuthUserModel> isUserExits = authUserRepository.findById(authUserId);

		if (isUserExits.isPresent()) {
			AuthUserModel user = isUserExits.get();
			String existingImageFileName = user.getAuthUserImage();
			try {
				// Create the upload directory if it doesn't exist
				Path uploadPath = Paths.get(UPLOAD_DIR);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				// If there is an existing image, delete it
				if (existingImageFileName != null && !existingImageFileName.isEmpty()) {
					Path existingImagePath = uploadPath.resolve(existingImageFileName);
					try {
						Files.delete(existingImagePath);
					} catch (IOException e) {
						return new ApiResponse<>("error", "Failed to delete existing image: " + e.getMessage(), null);
					}
				}
				String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
				Path newFilePath = uploadPath.resolve(newFileName);
				Files.copy(file.getInputStream(), newFilePath);
				user.setAuthUserImage(newFileName);
				authUserRepository.save(user);

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.POST);
				actionLogData.setActionLogMessage("Auth image uploaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return new ApiResponse<>("success", "Auth image uploaded successfully.", newFileName);
			} catch (Exception e) {
				return new ApiResponse<>("error", "Failed to upload auth image: " + e.getMessage(), null);
			}
		} else {
			return new ApiResponse<>("not found", "Auth user ID not found.", null);
		}
	}

	@Override
	public ApiResponse<List<AuthUserDTO>> getAllAuthUsers() {
		List<AuthUserModel> fetchAllAuthUsers = authUserRepository.findAll();

		if (fetchAllAuthUsers.isEmpty()) {
			return new ApiResponse<>("not found", "No auth user(s) found.", null);
		}

		List<AuthUserDTO> dtos = fetchAllAuthUsers.stream().map(AuthUserMapper::toDTO).toList();
		return new ApiResponse<>("success", "All auth users fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<AuthUserDTO> getAuthUser(long authUserId) {
		Optional<AuthUserModel> fetchAuthUser = authUserRepository.findById(authUserId);

		if (fetchAuthUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user not found.", null);
		}

		return new ApiResponse<>("success", "Auth user fetched successfully.",
				AuthUserMapper.toDTO(fetchAuthUser.get()));
	}

	@Override
	public ApiResponse<AuthUserDTO> updateAuthUser(long authUserId, AuthUserModel authUserInfo) {
		Optional<AuthUserModel> fetchAuthUser = authUserRepository.findById(authUserInfo.getActionByUserId());
		if (fetchAuthUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user not found.", null);
		}
		AuthUserModel fetchedAuthUser = fetchAuthUser.get();

		// Update only the fields that are being modified
		fetchedAuthUser.setAuthUserName(authUserInfo.getAuthUserName());
		fetchedAuthUser.setAuthUserEmailAddress(authUserInfo.getAuthUserEmailAddress());
		fetchedAuthUser.setAuthUserPhoneNumber(authUserInfo.getAuthUserPhoneNumber());
		fetchedAuthUser.setAuthUserActive(AuthUserActive.YES);
		fetchedAuthUser.setAuthUserUpdatedAt(ZonedDateTime.now());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Auth user updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		AuthUserModel updateAuthUserInfo = authUserRepository.save(fetchedAuthUser);
		return new ApiResponse<>("success", "Auth user updated successfully.",
				AuthUserMapper.toDTO(updateAuthUserInfo));
	}

	@Override
	public ApiResponse<AuthUserDTO> deleteAuthUser(long authUserId, long rqstAuthUserId) {
		Optional<AuthUserModel> fetchAuthUser = authUserRepository.findById(rqstAuthUserId);

		if (fetchAuthUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Auth user deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		authUserRepository.deleteById(rqstAuthUserId);
		return new ApiResponse<>("success", "Auth user deleted successfully.", null);
	}

	@Override
	public ApiResponse<Void> deleteAllAuthUsers(long authUserId, List<Long> rqstAuthUserIds) {
		for (Long rqstAuthUserId : rqstAuthUserIds) {
			if (!authUserRepository.existsById(rqstAuthUserId)) {
				return new ApiResponse<>("not found", "Some auth users were not found.", null);
			}
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("All specified auth users are deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		authUserRepository.deleteAllById(rqstAuthUserIds);
		return new ApiResponse<>("success", "All specified auth users are deleted successfully.", null);
	}
}