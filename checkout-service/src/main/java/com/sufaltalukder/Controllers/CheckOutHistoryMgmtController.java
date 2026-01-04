package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Services.CheckOutHistoryMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;
import com.sufaltalukder.feign.Services.AddToCartFeignService;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class CheckOutHistoryMgmtController {

	@Autowired
	private CheckOutHistoryMgmtService checkOutHistoryMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@Autowired
	private AddToCartFeignService addToCartFeignService; // feign client

	@PostMapping("/create-checkout-history")
	public ResponseEntity<ApiResponse<CheckOutDTO>> createUserCheckOut(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long userId,
			@RequestBody CheckOutHistoryModel checkOutHistoryModel) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<CheckOutDTO> response = checkOutHistoryMgmtService.createUserCheckOut(authUserId, userId,
					checkOutHistoryModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			if ("success".equals(response.getStatus())) {
				// call micro-service via feign client
				addToCartFeignService.deleteAllUserCarts(checkOutHistoryModel.getAddToCartIds(), userId);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-checkout-histories")
	public ResponseEntity<ApiResponse<List<CheckOutHistoryDTO>>> getAllCheckOutHistories(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {

		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<CheckOutHistoryDTO>> response = checkOutHistoryMgmtService.getAllCheckOutHistories();

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
