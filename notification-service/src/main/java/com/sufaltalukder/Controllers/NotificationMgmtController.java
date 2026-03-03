package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.DTOs.NotificationRequest;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Services.NotificationMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/elastic/auth")
@RequiredArgsConstructor
public class NotificationMgmtController {

	@Autowired
	private NotificationMgmtService notificationMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/push-mgmt-notification")
	public ResponseEntity<ApiResponse<NotificationDTO>> pushInAppNotification(
			@Valid @RequestBody NotificationRequest notificationRequest) {

		ApiResponse<NotificationDTO> response = notificationMgmtService.pushMgmtNotification(notificationRequest);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/get-all-mgmt-notifications")
	public ResponseEntity<ApiResponse<List<NotificationDTO>>> getAllMgmtNotifications(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {

		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<NotificationDTO>> response = notificationMgmtService.getAllMgmtNotifications();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping("/update-mgmt-notification")
	public ResponseEntity<ApiResponse<NotificationDTO>> markAsReadMgmtNotification(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long notificationId) {

		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<NotificationDTO> response = notificationMgmtService.markAsReadMgmtNotification(authUserId,
					notificationId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/remove-mgmt-notification")
	public ResponseEntity<ApiResponse<NotificationDTO>> removeMgmtNotification(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long notificationId) {

		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<NotificationDTO> response = notificationMgmtService.removeMgmtNotification(notificationId);

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
