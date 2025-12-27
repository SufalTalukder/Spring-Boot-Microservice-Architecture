package com.sufaltalukder.Services;

import java.security.SecureRandom;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Mappers.NewsletterMapper;
import com.sufaltalukder.Mappers.UserMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.OtpModel;
import com.sufaltalukder.Models.OtpResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.UserModel.UserActive;
import com.sufaltalukder.Repositories.NewsletterRepository;
import com.sufaltalukder.Repositories.OtpRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.Utils.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private NewsletterRepository newsletterRepository;

	@Autowired
	private JwtUtil jwtUtil;

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<OtpResponse> requestPhoneNumber(String phoneNumber) {

		List<UserModel> existingUsers = userRepository.findByPhoneNumber(phoneNumber);
		List<OtpModel> existingOtpRecords = otpRepository.findByPhoneNumber(phoneNumber);

		String generatedOtp = generateOtp();
		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));
		LocalDateTime expirationTime = currentTime.plusMinutes(5);

		UserModel targetUser = null;
		OtpModel targetOtp = null;

		// Pick the first matching record if duplicates exist (temporary safeguard)
		if (existingUsers != null && !existingUsers.isEmpty()) {
			targetUser = existingUsers.get(0);
		}
		if (existingOtpRecords != null && !existingOtpRecords.isEmpty()) {
			targetOtp = existingOtpRecords.get(0);
		}

		// Case 1: Both user and OTP record exist → update them
		if (targetUser != null && targetOtp != null) {
			targetUser.setPhoneNumber(phoneNumber);
			targetUser.setUserActive(UserActive.YES);
			userRepository.save(targetUser);

			UserModel user = userRepository.findById(targetUser.getUserId())
					.orElseThrow(() -> new RuntimeException("User not found"));

			targetOtp.setUserInfo(user);
			targetOtp.setPhoneNumber(phoneNumber);
			targetOtp.setOtp(generatedOtp);
			targetOtp.setOtpVerified(false);
			targetOtp.setOtpExpired(expirationTime);
			otpRepository.save(targetOtp);

			return new ApiResponse<>("success", "OTP sent to your phone number.", new OtpResponse(generatedOtp));
		}

		// Case 2: User exists but no OTP record
		if (targetUser != null) {
			saveOtpForUser(targetUser.getUserId(), phoneNumber, generatedOtp, expirationTime);
			return new ApiResponse<>("success", "OTP sent to your phone number.", new OtpResponse(generatedOtp));
		}

		// Case 3: New user (no user & no OTP)
		UserModel newUser = new UserModel();
		newUser.setPhoneNumber(phoneNumber);
		newUser.setUserActive(UserActive.YES);
		UserModel savedUser = userRepository.save(newUser);

		saveOtpForUser(savedUser.getUserId(), phoneNumber, generatedOtp, expirationTime);

		return new ApiResponse<>("success", "OTP sent to your phone number.", new OtpResponse(generatedOtp));
	}

	// Random OTP generator
	private String generateOtp() {
		SecureRandom random = new SecureRandom();
		return String.valueOf(100000 + random.nextInt(900000));
	}

	// To create and save the OTP record
	private void saveOtpForUser(long userId, String phoneNumber, String otp, LocalDateTime expirationTime) {

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		OtpModel otpRecord = new OtpModel();
		otpRecord.setUserInfo(user);
		otpRecord.setPhoneNumber(phoneNumber);
		otpRecord.setOtp(otp);
		otpRecord.setOtpVerified(false);
		otpRecord.setOtpExpired(expirationTime);
		otpRepository.save(otpRecord);
	}

	@Override
	public ApiResponse<AuthTokenResponse> verifyOtp(String phoneNumber, String otp) {

		List<UserModel> users = userRepository.findByPhoneNumber(phoneNumber);
		List<OtpModel> otpRecords = otpRepository.findByPhoneNumber(phoneNumber);

		// Select the first record if duplicates exist (temporary safeguard)
		UserModel user = (users != null && !users.isEmpty()) ? users.get(0) : null;
		OtpModel otpRecord = (otpRecords != null && !otpRecords.isEmpty()) ? otpRecords.get(0) : null;

		if (user == null || otpRecord == null) {
			return new ApiResponse<>("not found", "User or OTP record not found.", new AuthTokenResponse(null));
		}

		if (!otpRecord.getOtp().equals(otp)) {
			return new ApiResponse<>("not matched", "Invalid OTP.", new AuthTokenResponse(null));
		}

		LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("UTC"));
		if (currentTime.isAfter(otpRecord.getOtpExpired())) {
			return new ApiResponse<>("expired", "OTP expired.", new AuthTokenResponse(null));
		}

		otpRecord.setOtpVerified(true);
		otpRepository.save(otpRecord);

		// Generate unique referral code if missing
		if (user.getUserReferralCode() == null || user.getUserReferralCode().isEmpty()) {
			String uniqueReferral = generateUniqueReferralCode();
			user.setUserReferralCode(uniqueReferral);
			userRepository.save(user);
		}

		String generateAuthToken = jwtUtil.generateToken(phoneNumber, user.getUserId());

		return new ApiResponse<>("matched", "OTP verified successfully.", new AuthTokenResponse(generateAuthToken));
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
	public ApiResponse<UserDTO> fetchUser(long userId) {

		Optional<UserModel> isUserExist = userRepository.findById(userId);

		if (isUserExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		return new ApiResponse<>("success", "User detail fetched successfully.", UserMapper.toDTO(isUserExist.get()));
	}

	@Override
	public ApiResponse<String> fetchUserReferralCode(long userId) {

		Optional<UserModel> isUserExist = userRepository.findById(userId);

		if (isUserExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}
		String getUserReferralCode = isUserExist.get().getUserReferralCode();

		return new ApiResponse<>("success", "User referral fetched successfully.", getUserReferralCode);
	}

	@Override
	public ApiResponse<String> uploadImage(long userId, MultipartFile file) {

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

				return new ApiResponse<>("success", "User image uploaded successfully.", newFileName);

			} catch (Exception e) {
				return new ApiResponse<>("error", "Failed to upload user image: " + e.getMessage(), null);
			}
		} else {
			return new ApiResponse<>("not found", "User ID not found.", null);
		}
	}

	@Override
	public ApiResponse<UserDTO> updateDetail(long userId, UserModel userModel) {

		Optional<UserModel> isUserExist = userRepository.findById(userId);

		if (isUserExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		UserModel newUserData = isUserExist.get();
		newUserData.setFullName(userModel.getFullName());
		newUserData.setEmailAddress(userModel.getEmailAddress());
		newUserData.setDob(userModel.getDob());
		newUserData.setUserAddress(userModel.getUserAddress());
		newUserData.setUserUpdatedAt(ZonedDateTime.now());
		UserModel updateUser = userRepository.save(newUserData);

		return new ApiResponse<>("success", "User detail updated successfully.", UserMapper.toDTO(updateUser));
	}

	@Override
	public ApiResponse<NewsletterDTO> getNewsletterSubscribed(long userId) {

		Optional<UserModel> user = userRepository.findById(userId);

		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		NewsletterModel existingNewsletter = newsletterRepository.findByUserId(userId);

		if (existingNewsletter == null) {
			return new ApiResponse<>("not found", "Newsletter subscription not found.", null);
		}

		return new ApiResponse<>("success", "Newsletter status fetched.", NewsletterMapper.toDTO(existingNewsletter));
	}

}