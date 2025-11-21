package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CartApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Services.ProductAddToCartService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class ProductAddToCartController {

	@Autowired
	private ProductAddToCartService productAddToCartService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/add-to-cart")
	public ResponseEntity<ApiResponse<ProductAddToCartDTO>> createUserCart(@RequestHeader String authToken,
			@RequestBody ProductAddToCartModel productAddToCartModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			productAddToCartModel.setUserId(userId);

			ApiResponse<ProductAddToCartDTO> response = productAddToCartService.createUserCart(productAddToCartModel);

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

	@GetMapping("/get-all-carts")
	public ResponseEntity<PaginationApiResponse<List<ProductAddToCartDTO>>> getUserCarts(
			@RequestHeader("authToken") String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "addToCartId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			PaginationApiResponse<List<ProductAddToCartDTO>> response = productAddToCartService.getUserCarts(userId,
					pageNo, pageSize, sortBy, sortDir);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PatchMapping("/update-multi-carts")
	public ResponseEntity<CartApiResponse<List<ProductAddToCartDTO>>> updateUserMultiCarts(
			@RequestHeader String authToken, @RequestBody List<ProductAddToCartModel> cartUpdateModels) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			CartApiResponse<List<ProductAddToCartDTO>> response = productAddToCartService
					.updateUserMultiCarts(cartUpdateModels, userId);

			if ("not found".equalsIgnoreCase(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CartApiResponse<>("error", "Unauthorized access.", null, 0));
		}
	}

	@DeleteMapping("/remove-cart")
	public ResponseEntity<ApiResponse<Void>> removeUserCart(@RequestHeader String authToken,
			@RequestParam long addToCartId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<Void> response = productAddToCartService.removeUserCart(addToCartId, userId);

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
