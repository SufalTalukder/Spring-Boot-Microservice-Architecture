package com.sufaltalukder.Services;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.OtpResponse;
import com.sufaltalukder.Models.UserModel;

public interface UserService {

	ApiResponse<OtpResponse> requestPhoneNumber(String phoneNumber);

	ApiResponse<AuthTokenResponse> verifyOtp(String phoneNumber, String otp);

	ApiResponse<UserDTO> fetchUser(long userId);

	ApiResponse<String> uploadImage(long userId, MultipartFile file);

	ApiResponse<UserDTO> updateDetail(long userId, UserModel userModel);

	ApiResponse<String> fetchUserReferralCode(long userId);

	ApiResponse<NewsletterDTO> getNewsletterSubscribed(long userId);

}
