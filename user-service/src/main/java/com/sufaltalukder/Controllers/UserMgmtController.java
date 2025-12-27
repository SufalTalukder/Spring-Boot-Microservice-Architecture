package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Services.UserMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class UserMgmtController {

	@Autowired
	private UserMgmtService userMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/create-user")
	public ResponseEntity<ApiResponse<UserDTO>> createUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody UserModel userInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<UserDTO> response = userMgmtService.createUser(authUserId, userInfo);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-user-details")
	public ResponseEntity<ApiResponse<UserDTO>> fetchUserDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<UserDTO> response = userMgmtService.fetchUserDetails(authUserId, userId);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-users-list")
	public ResponseEntity<ApiResponse<List<UserDTO>>> fetchUsersList(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<UserDTO>> response = userMgmtService.fetchUsersList();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PostMapping("/upload-user-image")
	public ResponseEntity<ApiResponse<String>> uploadUserImage(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId,
			@RequestParam("image") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<String> response = userMgmtService.uploadUserImage(authUserId, userId, file);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PatchMapping("/update-user-details")
	public ResponseEntity<ApiResponse<UserDTO>> updateUserDetail(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId,
			@RequestBody UserModel userInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<UserDTO> response = userMgmtService.updateUserDetail(authUserId, userId, userInfo);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-user")
	public ResponseEntity<ApiResponse<UserDTO>> deleteUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<UserDTO> response = userMgmtService.deleteUser(authUserId, userId);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
