package com.sufaltalukder.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.NewsletterDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NewsletterModel;
import com.sufaltalukder.Services.NewsletterService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class NewsletterController {

	@Autowired
	private NewsletterService newsletterService;

	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/get-newsletter-subscription")
	public ResponseEntity<ApiResponse<NewsletterDTO>> getNewsletterToggle(@RequestParam long userId) {

		ApiResponse<NewsletterDTO> response = newsletterService.getNewsletterToggle(userId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/update-newsletter-subscription")
	public ResponseEntity<ApiResponse<NewsletterDTO>> updateNewsletterToggle(
			@RequestHeader("authToken") String authToken, @RequestBody NewsletterModel newsletterModel) {

		try {
			long userId = jwtUtil.extractUserId(authToken);

			ApiResponse<NewsletterDTO> response = newsletterService.updateNewsletterToggle(userId, newsletterModel);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
