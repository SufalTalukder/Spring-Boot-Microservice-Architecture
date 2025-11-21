package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.OrderReturnDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.OrderReturnModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Services.OrderReturnService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/order-return-api")
public class OrderReturnController {

	@Autowired
	private OrderReturnService orderReturnService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<OrderReturnDTO>> createUserOrderReturn(@RequestHeader String authToken,
			@RequestBody OrderReturnModel orderReturnModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			orderReturnModel.setAuthUserId(authUserId);

			ApiResponse<OrderReturnDTO> response = orderReturnService.createUserOrderReturn(orderReturnModel);

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

	@GetMapping("/read")
	public ResponseEntity<ApiResponse<OrderReturnDTO>> getUserOrderReturn(@RequestHeader String authToken,
			@RequestParam long orderReturnId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<OrderReturnDTO> response = orderReturnService.getUserOrderReturn(orderReturnId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/read-all/user-returns")
	public ResponseEntity<PaginationApiResponse<List<OrderReturnDTO>>> getUserAllOrderReturns(
			@RequestHeader("authToken") String authToken, @RequestParam long userId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "orderReturnId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<OrderReturnDTO>> response = orderReturnService.getUserAllOrderReturns(userId,
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

	@GetMapping("/read-all")
	public ResponseEntity<PaginationApiResponse<List<OrderReturnDTO>>> getAllUserOrderReturn(
			@RequestHeader("authToken") String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {

		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<OrderReturnDTO>> response = orderReturnService.getAllUserOrderReturn(pageNo,
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

	@PatchMapping("/update")
	public ResponseEntity<ApiResponse<OrderReturnDTO>> updateUserOrderReturn(@RequestHeader String authToken,
			@RequestParam long orderReturnId, @RequestBody OrderReturnModel orderReturnModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			orderReturnModel.setAuthUserId(authUserId);

			ApiResponse<OrderReturnDTO> response = orderReturnService.updateUserOrderReturn(orderReturnId,
					orderReturnModel);

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
