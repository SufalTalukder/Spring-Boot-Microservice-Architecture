package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Services.ActionLogMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class ActionLogMgmtController {

	@Autowired
	private ActionLogMgmtService actionLogMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/add-action-log")
	public ResponseEntity<ApiResponse<ActionLogDTO>> addActionLog(@RequestBody ActionLogModel actionLogModel) {

		ApiResponse<ActionLogDTO> response = actionLogMgmtService.addActionLog(actionLogModel);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/get-auth-action-logs")
	public ResponseEntity<ApiResponse<List<ActionLogDTO>>> getAuthActionLogs(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<ActionLogDTO>> response = actionLogMgmtService.getAuthActionLogs(authUserId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-user-action-logs-by-auth")
	public ResponseEntity<ApiResponse<List<ActionLogDTO>>> getUserActionLogsByAuth(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam long rqstAuthUserId) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<ActionLogDTO>> response = actionLogMgmtService.getUserActionLogsByAuth(rqstAuthUserId);

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
