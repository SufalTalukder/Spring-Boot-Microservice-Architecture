package com.sufaltalukder.Controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.*;
import com.sufaltalukder.Models.*;
import com.sufaltalukder.Repositories.*;
import com.sufaltalukder.Services.AuthUserMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/elastic/auth")
@RequiredArgsConstructor
public class AuthUserMgmtController {

	@Autowired
	private AuthUserMgmtService authUserMgmtService;
	@Autowired
	private AuthJwtUtil authJwtUtil;
	@Autowired
	private AuthUserRepository authUserRepository;
	@Autowired
	private AuthUserRefreshTokenRepository authUserRefreshTokenRepository;

	/** Only SUPER_ADMIN can create other admin/user accounts. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<AuthUserDTO>> createAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@Valid @ModelAttribute AuthUserRequest authUserInfo,
			@RequestPart(value = "authUserImage", required = false) MultipartFile authUserImage) {
		try {
			long actionByUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.createAuthUser(actionByUserId, authUserInfo,
					authUserImage);

			if ("required".equals(response.getStatus()) || "weak password".equals(response.getStatus())
					|| "invalid password".equals(response.getStatus())) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Public self-registration — verified only by API key/secret. */
	@PostMapping("/create-account")
	public ResponseEntity<ApiResponse<AuthUserDTO>> createAccount(
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@Valid @ModelAttribute AuthUserRequest authUserInfo) {
		try {
			authJwtUtil.verifyAuthUser(apiKey, apiSecret);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.createAccount(authUserInfo);

			if ("required".equals(response.getStatus()) || "weak password".equals(response.getStatus())
					|| "invalid password".equals(response.getStatus())) {
				return ResponseEntity.badRequest().body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Public — no JWT required. */
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthTokenResponse>> loginAuthUser(
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody RequestAuthLoginDTO requestAuthLoginDTO, HttpServletRequest request) {
		try {
			authJwtUtil.verifyAuthUser(apiKey, apiSecret);
			ApiResponse<AuthTokenResponse> response = authUserMgmtService.loginAuthUser(requestAuthLoginDTO, request);

			if (!"success".equals(response.getStatus()))
				return ResponseEntity.badRequest().body(response);

			return ResponseEntity.ok(response);

		} catch (SecurityException se) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", se.getMessage(), null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>("error", "Provided email or password doesn't match.", null));
		}
	}

	/** Public — only refresh-token required. */
	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<AuthTokenResponse>> refreshToken(
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody RefreshTokenRequest request) {

		authJwtUtil.verifyAuthUser(apiKey, apiSecret);

		long authUserId = authJwtUtil.extractAuthUserId(request.getRefreshToken(), apiKey, apiSecret);

		AuthUserRefreshTokenModel tokenEntity = authUserRefreshTokenRepository
				.findByRefreshTokenAndIsRevokedFalse(request.getRefreshToken())
				.orElseThrow(() -> new SecurityException("Invalid refresh token"));

		if (tokenEntity.getExpiryDate().isBefore(Instant.now())) {
			tokenEntity.setRevoked(true);
			authUserRefreshTokenRepository.save(tokenEntity);
			throw new SecurityException("Refresh token expired");
		}

		AuthUserModel user = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new SecurityException("User not found"));

		String newAccessToken = authJwtUtil.generateAccessToken(user.getAuthUserEmailAddress(), authUserId,
				user.getAuthUserType().name());

		return ResponseEntity.ok(new ApiResponse<>("success", "Access token refreshed",
				new AuthTokenResponse(newAccessToken, request.getRefreshToken())));
	}

	/** Public — revoke refresh token. */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<String>> logout(
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody LogoutRequest request) {

		authJwtUtil.verifyAuthUser(apiKey, apiSecret);

		AuthUserRefreshTokenModel token = authUserRefreshTokenRepository
				.findByRefreshTokenAndIsRevokedFalse(request.getRefreshToken())
				.orElseThrow(() -> new SecurityException("Invalid refresh token"));

		token.setRevoked(true);
		authUserRefreshTokenRepository.save(token);

		return ResponseEntity.ok(new ApiResponse<>("success", "Logged out successfully", null));
	}

	/** Both SUPER_ADMIN and ADMIN can upload their own profile image. */
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
	@PostMapping("/upload-image")
	public ResponseEntity<ApiResponse<String>> uploadImage(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam("authImage") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<String> response = authUserMgmtService.uploadImage(authUserId, file);

			if (!"success".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Both can see list of auth users. */
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
	@GetMapping("/get-all-auth-users")
	public ResponseEntity<ApiResponse<List<AuthUserDTO>>> getAllAuthUsers(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<List<AuthUserDTO>> response = authUserMgmtService.getAllAuthUsers();

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Both roles can view their own profile. */
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
	@GetMapping("/get-auth")
	public ResponseEntity<ApiResponse<AuthResponseDTO>> getAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthResponseDTO> response = authUserMgmtService.getAuthUser(authUserId);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can view details of any user by ID. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/get-auth-details")
	public ResponseEntity<ApiResponse<AuthUserDTO>> getAuthUserDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long authUserId) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.getAuthUserDetails(authUserId);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can read all login audit records. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/get-auth-login-audits")
	public ResponseEntity<ApiResponse<List<AuthLoginAuditDTO>>> getAuthUserLoginAudits(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<List<AuthLoginAuditDTO>> response = authUserMgmtService.getAuthUserLoginAudits();

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can view a specific audit record. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/get-auth-login-audit-details")
	public ResponseEntity<ApiResponse<AuthLoginAuditDTO>> getAuthUserLoginAuditDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long authLoginAuditId) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthLoginAuditDTO> response = authUserMgmtService
					.getAuthUserLoginAuditDetails(authLoginAuditId);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/**
	 * SUPER_ADMIN can update anyone. ADMIN can only update their own record —
	 * enforced in the service layer.
	 */
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
	@PutMapping(value = "/update-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<AuthUserDTO>> updateAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long authUserId,
			@Valid @ModelAttribute AuthUserRequest authUserInfo,
			@RequestPart(value = "authUserImage", required = false) MultipartFile authUserImage) {
		try {
			long actionByUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.updateAuthUser(actionByUserId, authUserId,
					authUserInfo, authUserImage);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can delete a user. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<AuthUserDTO>> deleteAuthUser(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long rqstAuthUserId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.deleteAuthUser(authUserId, rqstAuthUserId);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can bulk-delete users. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@DeleteMapping("/delete-all")
	public ResponseEntity<ApiResponse<Void>> deleteAllAuthUsers(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody List<Long> rqstAuthUserIds) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<Void> response = authUserMgmtService.deleteAllAuthUsers(authUserId, rqstAuthUserIds);

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can grant permissions. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping("/grant-permission")
	public ResponseEntity<ApiResponse<AuthPermissionDTO>> grantAuthPermission(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@Valid @RequestBody AuthPermissionRequest authPermissionRequest) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<AuthPermissionDTO> response = authUserMgmtService.grantAuthPermission(authUserId,
					authPermissionRequest);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	/** Only SUPER_ADMIN can view all permission records. */
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/get-auths-all-permissions")
	public ResponseEntity<ApiResponse<List<AuthPermissionDTO>>> getAuthsAllPermissions(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			ApiResponse<List<AuthPermissionDTO>> response = authUserMgmtService.getAuthsAllPermissions();

			if ("not found".equals(response.getStatus()))
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}