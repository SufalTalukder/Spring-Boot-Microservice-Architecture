package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.UserRatingDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserRatingModel;
import com.sufaltalukder.Services.UserRatingService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class UserRatingController {

	@Autowired
	private UserRatingService userRatingService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/add-user-rating")
	public ResponseEntity<ApiResponse<UserRatingDTO>> addUserRating(@RequestHeader("authToken") String authToken,
			@RequestBody UserRatingModel userRatingModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			userRatingModel.setUserId(userId);

			ApiResponse<UserRatingDTO> response = userRatingService.addUserRating(userRatingModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-product-ratings")
	public ResponseEntity<PaginationApiResponse<List<UserRatingDTO>>> getProductAllRatings(
			@RequestHeader("authToken") String authToken, @RequestParam long productId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			jwtUtil.extractUserId(authToken);
			PaginationApiResponse<List<UserRatingDTO>> response = userRatingService.getProductAllRatings(productId,
					pageNo, pageSize);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PutMapping("/update-user-rating")
	public ResponseEntity<ApiResponse<UserRatingDTO>> updateUserRating(@RequestHeader("authToken") String authToken,
			@RequestParam long userRatingId, @RequestBody UserRatingModel userRatingModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			userRatingModel.setUserId(userId);

			ApiResponse<UserRatingDTO> response = userRatingService.updateUserRating(userRatingId, userRatingModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-user-rating")
	public ResponseEntity<ApiResponse<UserRatingDTO>> deleteUserRating(@RequestHeader("authToken") String authToken,
			@RequestParam long productId, @RequestParam long userRatingId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);

			ApiResponse<UserRatingDTO> response = userRatingService.deleteUserRating(userId, productId, userRatingId);

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
