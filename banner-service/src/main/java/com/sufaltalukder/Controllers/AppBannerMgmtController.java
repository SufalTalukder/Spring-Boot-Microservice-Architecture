package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AppBannerDTO;
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

	@PostMapping("/upload-multi-images")
	public ResponseEntity<ApiResponse<List<AppBannerDTO>>> uploadMulipleImages(@RequestHeader String authToken,
			@RequestParam("appBannerImage") MultipartFile[] appBannerImage) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<List<AppBannerDTO>> response = appBannerMgmtService.uploadMulipleImages(authUserId,
					appBannerImage);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-banner-images")
	public ResponseEntity<ApiResponse<List<AppBannerDTO>>> fetchAllBannerImages(@RequestHeader String authToken) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
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
	public ResponseEntity<ApiResponse<List<String>>> deleteMultipleBannerImages(@RequestHeader String authToken,
			@RequestBody List<Long> appBannerIds) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<List<String>> response = appBannerMgmtService.deleteMultipleBannerImages(appBannerIds);

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
