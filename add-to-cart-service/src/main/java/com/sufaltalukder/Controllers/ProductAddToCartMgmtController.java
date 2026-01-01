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
