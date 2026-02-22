package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.DTOs.NewsletterRequest;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Services.NewsletterMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/elastic/auth")
@RequiredArgsConstructor
public class NewsletterMgmtController {

	@Autowired
	private NewsletterMgmtService newsletterMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/create-newsletter")
	public ResponseEntity<ApiResponse<NewsletterDTO>> createNewsletterToggle(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@Valid @RequestBody NewsletterRequest newsletterRequest) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<NewsletterDTO> response = newsletterMgmtService.createNewsletterToggle(authUserId,
					newsletterRequest);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-newsletter-details")
	public ResponseEntity<ApiResponse<NewsletterDTO>> getNewsletterToggle(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long newsletterId) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<NewsletterDTO> response = newsletterMgmtService.getNewsletterToggle(authUserId, newsletterId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-newsletters")
	public ResponseEntity<ApiResponse<List<NewsletterDTO>>> getAllNewsletterToggle(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {

		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<NewsletterDTO>> response = newsletterMgmtService.getAllNewsletterToggle();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PatchMapping("/update-newsletter-details")
	public ResponseEntity<ApiResponse<NewsletterDTO>> updateNewsletterToggle(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long newsletterId,
			@Valid @RequestBody NewsletterRequest newsletterRequest) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<NewsletterDTO> response = newsletterMgmtService.updateNewsletterToggle(newsletterId, authUserId,
					newsletterRequest);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
