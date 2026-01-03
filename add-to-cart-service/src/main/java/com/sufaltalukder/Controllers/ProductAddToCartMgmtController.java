package com.sufaltalukder.Controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Services.ProductAddToCartMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class ProductAddToCartMgmtController {

	@Autowired
	private ProductAddToCartMgmtService productAddToCartMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/add-user-cart")
	public ResponseEntity<ApiResponse<ProductAddToCartDTO>> addUserCart(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam Long userId,
			@RequestParam Long productId, @RequestBody ProductAddToCartModel productAddToCartModel) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<ProductAddToCartDTO> response = productAddToCartMgmtService.addUserCart(authUserId, userId,
					productId, productAddToCartModel);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-carts")
	public ResponseEntity<ApiResponse<List<ProductAddToCartDTO>>> getAllCarts(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam(value = "userId", required = false) long userId) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<ProductAddToCartDTO>> response = productAddToCartMgmtService.getAllCarts(authUserId,
					userId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-product-price")
	public ResponseEntity<Double> getPriceOfSelectedProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam(value = "productId", required = false) long productId) {

		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			Double productPrice = productAddToCartMgmtService.getPriceOfSelectedProduct(productId);

			if (productPrice == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}

			return ResponseEntity.ok(productPrice);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@DeleteMapping("/delete-user-cart")
	public ResponseEntity<ApiResponse<Void>> removeUserCart(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long addToCartId,
			@RequestParam long userId) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<Void> response = productAddToCartMgmtService.removeUserCart(authUserId, addToCartId, userId);

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

	// feign client
	@GetMapping("/get-user-cart")
	public ResponseEntity<ApiResponse<ProductAddToCartModel>> getUserCart(@RequestParam long addToCartId,
			@RequestParam long userId) {

		ApiResponse<ProductAddToCartModel> response = productAddToCartMgmtService.getUserCart(addToCartId, userId);

		if ("not applicable".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		}

		return ResponseEntity.ok(response);
	}
}
