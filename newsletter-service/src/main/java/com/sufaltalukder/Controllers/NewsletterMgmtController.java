package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Services.NewsletterMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class NewsletterMgmtController {

	@Autowired
	private NewsletterMgmtService newsletterMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/create-newsletter")
	public ResponseEntity<ApiResponse<NewsletterDTO>> createNewsletterToggle(@RequestHeader String authToken,
			@RequestBody NewsletterModel newsletterModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			newsletterModel.setAuthUserId(authUserId);
			ApiResponse<NewsletterDTO> response = newsletterMgmtService.createNewsletterToggle(newsletterModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-newsletter")
	public ResponseEntity<ApiResponse<NewsletterDTO>> getNewsletterToggle(@RequestHeader String authToken,
			@RequestParam long newsletterId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<NewsletterDTO> response = newsletterMgmtService.getNewsletterToggle(newsletterId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-newsletter")
	public ResponseEntity<PaginationApiResponse<List<NewsletterDTO>>> getAllNewsletterToggle(
			@RequestHeader String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<NewsletterDTO>> response = newsletterMgmtService.getAllNewsletterToggle(pageNo,
					pageSize);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PatchMapping("/update-newsletter-details")
	public ResponseEntity<ApiResponse<NewsletterDTO>> updateNewsletterToggle(@RequestHeader String authToken,
			@RequestParam long newsletterId, @RequestBody NewsletterModel newsletterModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			newsletterModel.setAuthUserId(authUserId);
			ApiResponse<NewsletterDTO> response = newsletterMgmtService.updateNewsletterToggle(newsletterId,
					newsletterModel);

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
