package com.sufaltalukder.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.OtpResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Services.UserService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/request-phone-number")
	public ResponseEntity<ApiResponse<OtpResponse>> requestPhoneNumber(@RequestParam String phoneNumber) {
		ApiResponse<OtpResponse> response = userService.requestPhoneNumber(phoneNumber);

		if ("failed".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse<AuthTokenResponse>> verifyOtp(@RequestParam String phoneNumber,
			@RequestParam String otp) {
		ApiResponse<AuthTokenResponse> response = userService.verifyOtp(phoneNumber, otp);

		if ("expired".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
		} else if ("not matched".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	@PostMapping("/resend-otp")
	public ResponseEntity<ApiResponse<OtpResponse>> resendOtp(@RequestParam String phoneNumber) {
		ApiResponse<OtpResponse> response = userService.requestPhoneNumber(phoneNumber);
		if (!"success".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/get")
	public ResponseEntity<ApiResponse<UserDTO>> fetchUser(@RequestHeader("authToken") String authToken) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<UserDTO> response = userService.fetchUser(userId);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-referral")
	public ResponseEntity<ApiResponse<String>> fetchUserReferralCode(@RequestHeader("authToken") String authToken) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<String> response = userService.fetchUserReferralCode(userId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PostMapping("/upload-image")
	public ResponseEntity<ApiResponse<String>> uploadImage(@RequestHeader("authToken") String authToken,
			@RequestParam("image") MultipartFile file) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<String> response = userService.uploadImage(userId, file);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PatchMapping("/update-details")
	public ResponseEntity<ApiResponse<UserDTO>> updateDetail(@RequestHeader("authToken") String authToken,
			@RequestBody UserModel userModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<UserDTO> response = userService.updateDetail(userId, userModel);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/is-newsletter-subscribed")
	public ResponseEntity<ApiResponse<NewsletterDTO>> getNewsletterSubscribed(@RequestParam long userId) {
		ApiResponse<NewsletterDTO> response = userService.getNewsletterSubscribed(userId);

		if ("not found".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		if ("not same".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		return ResponseEntity.ok(response);
	}
}
