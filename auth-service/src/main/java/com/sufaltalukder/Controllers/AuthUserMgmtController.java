package com.sufaltalukder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Services.AuthUserMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class AuthUserMgmtController {

	@Autowired
	private AuthUserMgmtService authUserMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthTokenResponse>> loginAuthUser(
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam String authUserEmailAddress, @RequestParam String authUserPassword,
			HttpServletRequest request) {
		try {
			authJwtUtil.verifyAuthUser(apiKey, apiSecret);

			ApiResponse<AuthTokenResponse> response = authUserMgmtService.loginAuthUser(authUserEmailAddress,
					authUserPassword, request);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.badRequest().body(response);
			}

			return ResponseEntity.ok(response);

		} catch (SecurityException se) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", se.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>("error", "Provided email or password doesn't match.", null));
		}
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<AuthUserDTO>> createAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody AuthUserModel authUserInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<AuthUserDTO> response = authUserMgmtService.createAuthUser(authUserId, authUserInfo);

			if ("required".equals(response.getStatus()) || "weak password".equals(response.getStatus())
					|| "invalid password".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PostMapping("/upload-image")
	public ResponseEntity<ApiResponse<String>> uploadImage(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam("authImage") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<String> response = authUserMgmtService.uploadImage(authUserId, file);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-auth-users")
	public ResponseEntity<ApiResponse<List<AuthUserDTO>>> getAllAuthUsers(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<AuthUserDTO>> response = authUserMgmtService.getAllAuthUsers();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-auth")
	public ResponseEntity<ApiResponse<AuthUserDTO>> getAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<AuthUserDTO> response = authUserMgmtService.getAuthUser(authUserId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-auth-details")
	public ResponseEntity<ApiResponse<AuthUserDTO>> getAuthUserDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long authUserId) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<AuthUserDTO> response = authUserMgmtService.getAuthUserDetails(authUserId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping("/update-details")
	public ResponseEntity<ApiResponse<AuthUserDTO>> updateAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long authUserId,
			@RequestBody AuthUserModel authUserInfo) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<AuthUserDTO> response = authUserMgmtService.updateAuthUser(authUserId, authUserInfo);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<AuthUserDTO>> deleteAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long rqstAuthUserId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<AuthUserDTO> response = authUserMgmtService.deleteAuthUser(authUserId, rqstAuthUserId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-all")
	public ResponseEntity<ApiResponse<Void>> deleteAllAuthUsers(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody List<Long> rqstAuthUserIds) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<Void> response = authUserMgmtService.deleteAllAuthUsers(authUserId, rqstAuthUserIds);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}