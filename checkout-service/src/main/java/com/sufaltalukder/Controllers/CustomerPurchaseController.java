package com.sufaltalukder.Controllers;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Services.CustomerPurchaseService;
import com.sufaltalukder.Utils.JwtUtil;
import com.sufaltalukder.feign.Services.AddToCartFeignService;
import com.sufaltalukder.feign.Services.NotificationFeignService;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class CustomerPurchaseController {

	@Autowired
	private CustomerPurchaseService customerPurchaseService;

	@Autowired
	private AddToCartFeignService addToCartFeignService; // addTocart feign client

	@Autowired
	private NotificationFeignService notificationFeignService; // notification feign client

	@Autowired
	private JwtUtil jwtUtil;

	private static final Logger logger = LoggerFactory.getLogger(CustomerPurchaseController.class);

	@PostMapping("/checkout-history")
	public ResponseEntity<ApiResponse<CheckOutHistoryDTO>> createUserCheckOut(
			@RequestHeader("authToken") String authToken, @RequestBody CheckOutHistoryModel checkOutHistoryModel) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			checkOutHistoryModel.setUserId(userId);

			ApiResponse<CheckOutHistoryDTO> response = customerPurchaseService.createUserCheckOut(checkOutHistoryModel);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			if ("success".equals(response.getStatus())) {

				List<String> productIds = response.getContent().getProductIds();

				for (String eachId : productIds) {

					long pid = Long.parseLong(eachId);

					NotificationModel model = new NotificationModel();
					model.setUserId(userId);
					model.setNotificationProductId(pid);

					// call notification micro-service via feign client
					notificationFeignService.pushInAppNotification(model);
				}

				// call addToCart micro-service via feign client
				addToCartFeignService.deleteAllUserCarts(checkOutHistoryModel.getAddToCartIds(),
						checkOutHistoryModel.getUserId());
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-purchase-details")
	public ResponseEntity<ApiResponse<CheckOutHistoryDTO>> getPurchaseDetails(
			@RequestHeader("authToken") String authToken, @RequestParam long checkOutHistoryId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);

			ApiResponse<CheckOutHistoryDTO> response = customerPurchaseService.getPurchaseDetails(userId,
					checkOutHistoryId);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-purchases")
	public ResponseEntity<PaginationApiResponse<List<CheckOutHistoryDTO>>> getAllPurchasesList(
			@RequestHeader("authToken") String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "checkOutHistoryId") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			long userId = jwtUtil.extractUserId(authToken);

			PaginationApiResponse<List<CheckOutHistoryDTO>> response = customerPurchaseService
					.getAllPurchasesList(userId, pageNo, pageSize, sortBy, sortDir);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PatchMapping("/cancel-purchase")
	public ResponseEntity<ApiResponse<CheckOutHistoryDTO>> cancelPurchase(@RequestHeader("authToken") String authToken,
			@RequestParam long checkOutHistoryId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);

			ApiResponse<CheckOutHistoryDTO> response = customerPurchaseService.cancelPurchase(userId,
					checkOutHistoryId);

			if (!"success".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}