package com.sufaltalukder.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<ApiResponse<CheckOutHistoryDTO>> createUserCheckOut(
			@RequestHeader("authToken") String authToken, @RequestBody CheckOutHistoryModel checkOutHistoryModel) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			checkOutHistoryModel.setAuthUserId(authUserId);

			ApiResponse<CheckOutHistoryDTO> response = checkOutHistoryMgmtService
					.createUserCheckOut(checkOutHistoryModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			if ("success".equals(response.getStatus())) {

				// call micro-service via feign client
				addToCartFeignService.deleteAllUserCarts(checkOutHistoryModel.getAddToCartIds(),
						checkOutHistoryModel.getUserId());
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
