package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Services.UserMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class UserMgmtController {

	@Autowired
	private UserMgmtService userMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping("/create-user")
	public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestHeader String authToken,
			@RequestBody UserModel userInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			userInfo.setAuthUserId(authUserId);
			ApiResponse<UserDTO> response = userMgmtService.createUser(userInfo);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-user")
	public ResponseEntity<ApiResponse<UserDTO>> getByUser(@RequestHeader String authToken, @RequestParam long userId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<UserDTO> response = userMgmtService.getByUser(userId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-users")
	public ResponseEntity<PaginationApiResponse<List<UserDTO>>> getAllUsers(@RequestHeader String authToken,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<UserDTO>> response = userMgmtService.getAllUsers(pageNo, pageSize);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PatchMapping("/update-user-details")
	public ResponseEntity<ApiResponse<UserDTO>> updateUser(@RequestHeader String authToken, @RequestParam long userId,
			@RequestBody UserModel userInfo) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			userInfo.setAuthUserId(authUserId);
			ApiResponse<UserDTO> response = userMgmtService.updateUser(userId, userInfo);

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

	@DeleteMapping("/delete-user")
	public ResponseEntity<ApiResponse<UserDTO>> deleteUser(@RequestHeader String authToken, @RequestParam long userId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<UserDTO> response = userMgmtService.deleteUser(userId);

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
