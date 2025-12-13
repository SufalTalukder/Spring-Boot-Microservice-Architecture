package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
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

	@GetMapping("/get-all-action-logs")
	public ResponseEntity<PaginationApiResponse<List<ActionLogDTO>>> getAllActionLogs(
			@RequestHeader("authToken") String authToken, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "25") int pageSize) {
		try {
			authJwtUtil.extractAuthUserId(authToken);

			PaginationApiResponse<List<ActionLogDTO>> response = actionLogMgmtService.getAllActionLogs(pageNo,
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

	@GetMapping("/get-all-user-action-logs")
	public ResponseEntity<PaginationApiResponse<List<ActionLogDTO>>> getAllAuthUserActionLogs(
			@RequestHeader("authToken") String authToken, @RequestParam long authUserId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "25") int pageSize) {
		try {
			authJwtUtil.extractAuthUserId(authToken);

			PaginationApiResponse<List<ActionLogDTO>> response = actionLogMgmtService
					.getAllAuthUserActionLogs(authUserId, pageNo, pageSize);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}
}
