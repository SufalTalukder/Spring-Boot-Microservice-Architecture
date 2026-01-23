package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.AppBannerDTO;
import com.sufaltalukder.DTOs.RequestBannerDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Services.AppBannerMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class AppBannerMgmtController {

	@Autowired
	private AppBannerMgmtService appBannerMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping(value = "/upload-multi-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<List<AppBannerDTO>>> uploadMulipleImages(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@ModelAttribute RequestBannerDTO requestBannerDTO) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<AppBannerDTO>> response = appBannerMgmtService.uploadMulipleImages(authUserId,
					requestBannerDTO);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-banner-images")
	public ResponseEntity<ApiResponse<List<AppBannerDTO>>> fetchAllBannerImages(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<AppBannerDTO>> response = appBannerMgmtService.fetchAllBannerImages();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-multi-images")
	public ResponseEntity<ApiResponse<List<String>>> deleteMultipleBannerImages(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody List<Long> appBannerIds) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<String>> response = appBannerMgmtService.deleteMultipleBannerImages(authUserId,
					appBannerIds);

			if ("partial".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
			}

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
