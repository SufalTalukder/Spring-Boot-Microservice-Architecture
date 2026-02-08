package com.sufaltalukder.Services;

import java.io.IOException;
import java.nio.file.*;
import java.security.SecureRandom;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.DTOs.UserRequest;
import com.sufaltalukder.Mappers.UserMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<UserDTO> createUser(long authUserId, @Valid UserRequest userInfo, MultipartFile userImage) {

		// Check if phone number already exists
		List<UserModel> existingUsers = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());

		if (existingUsers != null && !existingUsers.isEmpty()) {
			return new ApiResponse<>("exist", "Phone number already exists.", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel savingUserData = new UserModel();
		savingUserData.setAuthUserInfo(authUser);
		savingUserData.setFullName(userInfo.getFullName());
		savingUserData.setPhoneNumber(userInfo.getPhoneNumber());
		savingUserData.setEmailAddress(userInfo.getEmailAddress());
		savingUserData.setDob(userInfo.getDob());
		savingUserData.setUserAddress(userInfo.getUserAddress());
		savingUserData.setUserActive(userInfo.getUserActive());

		// Generate referral code
		savingUserData.setUserReferralCode(generateUniqueReferralCode());

		UserModel savedData = userRepository.save(savingUserData);

		// Push action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("User created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User created successfully.", UserMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<UserDTO> fetchUserDetails(long authUserId, long userId) {

		UserModel findUserExists = userRepository.findUserDetailsByAuth(userId);

		if (findUserExists == null) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("User details fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User details fetched successfully.", UserMapper.toDTO(findUserExists));
	}

	@Override
	public ApiResponse<List<UserDTO>> fetchUsersList() {

		List<UserModel> findAllUsers = userRepository.findAllUsersByAuth();

		if (findAllUsers.isEmpty()) {
			return new ApiResponse<>("not found", "User(s) list not found.", null);
		}

		List<UserDTO> dtos = findAllUsers.stream().map(UserMapper::toDTO).toList();

		return new ApiResponse<>("success", "User(s) list fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<String> uploadUserImage(long authUserId, long userId, MultipartFile file) {

		Optional<UserModel> isUserExist = userRepository.findById(userId);

		if (isUserExist.isPresent()) {
			UserModel user = isUserExist.get();
			String existingImageFileName = user.getUserImage();
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
				user.setUserImage(newFileName);
				userRepository.save(user);

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.POST);
				actionLogData.setActionLogMessage("User image uploaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return new ApiResponse<>("success", "User image uploaded successfully.", newFileName);

			} catch (Exception e) {
				return new ApiResponse<>("error", "Failed to upload user image: " + e.getMessage(), null);
			}
		} else {
			return new ApiResponse<>("not found", "User ID not found.", null);
		}
	}

	@Override
	public ApiResponse<UserDTO> updateUserDetail(long authUserId, long userId, @Valid UserRequest userInfo,
			MultipartFile userImage) {

		UserModel existingUser = userRepository.findUserDetailsByAuth(userId);
		if (existingUser == null) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		// Check phone number uniqueness (allow same user's phone)
		List<UserModel> existingUsers = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());
		if (existingUsers != null && !existingUsers.isEmpty()) {
			UserModel userWithSamePhone = existingUsers.get(0);

			if (userWithSamePhone.getUserId() != existingUser.getUserId()) {
				return new ApiResponse<>("exist", "Phone number already exists.", null);
			}
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		existingUser.setAuthUserInfo(authUser);
		existingUser.setFullName(userInfo.getFullName());
		existingUser.setPhoneNumber(userInfo.getPhoneNumber());
		existingUser.setEmailAddress(userInfo.getEmailAddress());
		existingUser.setDob(userInfo.getDob());
		existingUser.setUserAddress(userInfo.getUserAddress());
		existingUser.setUserActive(userInfo.getUserActive());

		if (existingUser.getUserReferralCode() == null || existingUser.getUserReferralCode().isEmpty()) {

			existingUser.setUserReferralCode(generateUniqueReferralCode());
		}

		UserModel updatedData = userRepository.save(existingUser);

		// Push action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PATCH);
		actionLogData.setActionLogMessage("User updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User updated successfully.", UserMapper.toDTO(updatedData));
	}

	// Generate referral
	private String generateUniqueReferralCode() {
		String referralCode;
		do {
			referralCode = generateReferral();
		} while (userRepository.existsByUserReferralCode(referralCode));
		return referralCode;
	}

	private String generateReferral() {
		final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final int LENGTH = 6;
		SecureRandom random = new SecureRandom();
		StringBuilder code = new StringBuilder(LENGTH);
		for (int i = 0; i < LENGTH; i++) {
			code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
		}
		return code.toString();
	}

	@Override
	public ApiResponse<UserDTO> deleteUser(long authUserId, long userId) {

		Optional<UserModel> findUserExists = userRepository.findByUserId(userId);

		if (findUserExists.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		userRepository.deleteById(userId);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("User deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User deleted successfully.", null);
	}
}
