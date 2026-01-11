package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.SupportModel;
import com.sufaltalukder.Services.SupportMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class SupportMgmtController {

	@Autowired
	private SupportMgmtService supportMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/add-user-support")
	public ResponseEntity<ApiResponse<SupportDTO>> addUserSupport(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId,
			@RequestBody SupportModel supportModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<SupportDTO> response = supportMgmtService.addUserSupport(authUserId, userId, supportModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-user-support-details")
	public ResponseEntity<ApiResponse<SupportDTO>> getUserSupportDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId,
			@RequestParam long supportId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<SupportDTO> response = supportMgmtService.getUserSupportDetails(authUserId, userId, supportId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-user-supports")
	public ResponseEntity<ApiResponse<List<SupportDTO>>> getAllUserSupports(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<SupportDTO>> response = supportMgmtService.getAllUserSupports();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping("/update-user-support-details")
	public ResponseEntity<ApiResponse<SupportDTO>> updateUserSupportDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long supportId,
			@RequestParam long userId, @RequestBody SupportModel supportModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<SupportDTO> response = supportMgmtService.updateUserSupportDetails(authUserId, supportId,
					userId, supportModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-user-support-details")
	public ResponseEntity<ApiResponse<SupportDTO>> deleteUserSupportDetails(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long supportId,
			@RequestParam long userId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<SupportDTO> response = supportMgmtService.deleteUserSupportDetails(authUserId, userId,
					supportId);

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
