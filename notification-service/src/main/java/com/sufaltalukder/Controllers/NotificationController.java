package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Services.NotificationService;
import com.sufaltalukder.Utils.JwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/user")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/push-inapp-notification")
	public ResponseEntity<ApiResponse<NotificationDTO>> pushInAppNotification(
			@RequestBody NotificationModel notificationModel) {

		ApiResponse<NotificationDTO> response = notificationService.pushInAppNotification(notificationModel);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-all-pushed-inapp-notifications")
	public ResponseEntity<ApiResponse<List<NotificationDTO>>> getAllPushedInAppNotifications(
			@RequestHeader("authToken") String authToken) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<List<NotificationDTO>> response = notificationService.getAllPushedInAppNotifications(userId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/remove-inapp-notification")
	public ResponseEntity<ApiResponse<NotificationDTO>> removeInAppNotification(
			@RequestHeader("authToken") String authToken, @RequestParam long notificationId) {
		try {
			long userId = jwtUtil.extractUserId(authToken);
			ApiResponse<NotificationDTO> response = notificationService.removeInAppNotification(userId, notificationId);

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
