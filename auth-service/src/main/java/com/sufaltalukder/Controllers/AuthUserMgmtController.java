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

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class AuthUserMgmtController {

	@Autowired
	private AuthUserMgmtService authUserMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthTokenResponse>> loginAuthUser(@RequestParam String authUserEmailAddress,
			@RequestParam String authUserPassword) {
		ApiResponse<AuthTokenResponse> response = authUserMgmtService.loginAuthUser(authUserEmailAddress, authUserPassword);

		if ("not found".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if ("not matched".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<AuthUserDTO>> createAuthUser(@RequestBody AuthUserModel authUserInfo) {
		ApiResponse<AuthUserDTO> response = authUserMgmtService.createAuthUser(authUserInfo);

		if ("required".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if ("weak password".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if ("invalid password".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/upload-image")
	public ResponseEntity<ApiResponse<String>> uploadImage(@RequestHeader String authToken,
			@RequestParam("authImage") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
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
	public ResponseEntity<ApiResponse<List<AuthUserDTO>>> getAllAuthUsers(@RequestHeader String authToken) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
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

	@GetMapping("/get")
	public ResponseEntity<ApiResponse<AuthUserDTO>> getAuthUser(@RequestHeader String authToken) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
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

	@PutMapping("/update-details")
	public ResponseEntity<ApiResponse<AuthUserDTO>> updateAuthUser(@RequestHeader String authToken,
			@RequestBody AuthUserModel authUserInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			authUserInfo.setActionByUserId(authUserId);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.updateAuthUser(authUserInfo);

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
	public ResponseEntity<ApiResponse<AuthUserDTO>> deleteAuthUser(@RequestHeader String authToken) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<AuthUserDTO> response = authUserMgmtService.deleteAuthUser(authUserId);

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
	public ResponseEntity<ApiResponse<Void>> deleteAllAuthUsers(@RequestHeader String authToken,
			@RequestBody List<Long> authUserIds) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<Void> response = authUserMgmtService.deleteAllAuthUsers(authUserIds);

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