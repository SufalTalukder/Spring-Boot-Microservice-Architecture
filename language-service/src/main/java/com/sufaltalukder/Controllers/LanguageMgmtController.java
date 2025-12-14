package com.sufaltalukder.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Services.LanguageMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class LanguageMgmtController {

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@Autowired
	private LanguageMgmtService languageMgmtService;

	@PostMapping("/create-language")
	public ResponseEntity<ApiResponse<LanguageDTO>> createLanguage(@RequestHeader("authToken") String authToken,
			@RequestBody LanguageModel languageModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			languageModel.setAuthUserId(authUserId);

			ApiResponse<LanguageDTO> response = languageMgmtService.createLanguage(languageModel);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-language")
	public ResponseEntity<ApiResponse<LanguageDTO>> getLanguage(@RequestHeader("authToken") String authToken,
			@RequestParam long languageId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<LanguageDTO> response = languageMgmtService.getLanguage(authUserId, languageId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-languages")
	public ResponseEntity<ApiResponse<List<LanguageDTO>>> getAllLanguages(
			@RequestHeader("authToken") String authToken) {
		try {
			authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<List<LanguageDTO>> response = languageMgmtService.getAllLanguages();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping("/update-language-details")
	public ResponseEntity<ApiResponse<LanguageDTO>> updateLanguage(@RequestHeader("authToken") String authToken,
			@RequestParam long languageId, @RequestBody LanguageModel languageModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			languageModel.setAuthUserId(authUserId);

			ApiResponse<LanguageDTO> response = languageMgmtService.updateLanguage(languageId, languageModel);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@DeleteMapping("/delete-language")
	public ResponseEntity<ApiResponse<LanguageDTO>> deleteLanguage(@RequestHeader("authToken") String authToken,
			@RequestParam long languageId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<LanguageDTO> response = languageMgmtService.deleteLanguage(authUserId, languageId);

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
