package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;
import com.sufaltalukder.Services.ProductAddToFavouriteService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class ProductAddToFavouriteController {

	@Autowired
	private ProductAddToFavouriteService productAddToFavouriteService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/add-to-favourite")
	public ResponseEntity<ApiResponse<ProductAddToFavouriteDTO>> createUserFavourite(@RequestHeader String authToken,
			@RequestBody ProductAddToFavouriteModel productAddToFavouriteModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			productAddToFavouriteModel.setUserId(userId);

			ApiResponse<ProductAddToFavouriteDTO> response = productAddToFavouriteService
					.createUserFavourite(productAddToFavouriteModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-favourites")
	public ResponseEntity<PaginationApiResponse<List<ProductAddToFavouriteDTO>>> getUserFavourites(
			@RequestHeader("authToken") String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "addToFavouriteId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			PaginationApiResponse<List<ProductAddToFavouriteDTO>> response = productAddToFavouriteService
					.getUserFavourites(userId, pageNo, pageSize, sortBy, sortDir);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@DeleteMapping("/remove-favourite")
	public ResponseEntity<ApiResponse<Void>> removeUserFavourite(@RequestHeader String authToken,
			@RequestParam long addToFavouriteId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<Void> response = productAddToFavouriteService.removeUserFavourite(addToFavouriteId, userId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			if ("not applicable".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
