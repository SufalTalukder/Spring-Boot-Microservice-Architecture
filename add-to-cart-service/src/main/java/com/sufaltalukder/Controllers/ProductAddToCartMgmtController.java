package com.sufaltalukder.Controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CartApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
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

	@PostMapping("/create-add-to-cart")
	public ResponseEntity<ApiResponse<ProductAddToCartDTO>> createUserCart(@RequestHeader String authToken,
			@RequestBody ProductAddToCartModel productAddToCartModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			productAddToCartModel.setAuthUserId(authUserId);

			ApiResponse<ProductAddToCartDTO> response = productAddToCartMgmtService
					.createUserCart(productAddToCartModel);

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

	@PatchMapping("/update-user-multi-carts")
	public ResponseEntity<CartApiResponse<List<ProductAddToCartDTO>>> updateUserMultiCarts(
			@RequestHeader String authToken, @RequestParam long userId,
			@RequestBody List<ProductAddToCartModel> cartUpdateModels) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			CartApiResponse<List<ProductAddToCartDTO>> response = productAddToCartMgmtService
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

	// using for feign client
	@GetMapping("/get-user-cart")
	public ResponseEntity<ApiResponse<ProductAddToCartModel>> getUserCart(@RequestParam long addToCartId,
			@RequestParam long userId) {

		ApiResponse<ProductAddToCartModel> response = productAddToCartMgmtService.getUserCart(addToCartId, userId);

		if ("not applicable".equals(response.getStatus())) {
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-user-carts")
	public ResponseEntity<PaginationApiResponse<List<ProductAddToCartDTO>>> getUserCarts(
			@RequestHeader("authToken") String authToken, @RequestParam long userId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "addToCartId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<ProductAddToCartDTO>> response = productAddToCartMgmtService.getUserCarts(userId,
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

	@DeleteMapping("/delete-user-cart")
	public ResponseEntity<ApiResponse<Void>> removeUserCart(@RequestHeader String authToken,
			@RequestParam long addToCartId, @RequestParam long userId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<Void> response = productAddToCartMgmtService.removeUserCart(addToCartId, userId);

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

	@DeleteMapping("/delete-all-user-carts")
	public ResponseEntity<ApiResponse<Void>> removeUserAllCarts(@RequestHeader String authToken,
			@RequestParam String addToCartIds, @RequestParam long userId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<Void> response = productAddToCartMgmtService.removeUserAllCarts(addToCartIds, userId);

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
