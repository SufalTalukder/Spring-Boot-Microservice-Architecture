package com.sufaltalukder.Controllers;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.DTOs.RequestCategoryDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Services.CategoryMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;
import com.sufaltalukder.Utils.CsvCategoryUtils;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class CategoryMgmtController {

	@Autowired
	private CategoryMgmtService categoryMgmtService;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Autowired
	private CsvCategoryUtils csvUtils;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping(value = "/create-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,

			@ModelAttribute RequestCategoryDTO requestCategoryDTO,
			@RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<CategoryDTO> response = categoryMgmtService.createCategory(authUserId, requestCategoryDTO,
					categoryImage);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-category")
	public ResponseEntity<ApiResponse<CategoryDTO>> getCategory(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long categoryId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<CategoryDTO> response = categoryMgmtService.getCategory(authUserId, categoryId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-categories")
	public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAllCategories(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<CategoryDTO>> response = categoryMgmtService.getAllCategories();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping(value = "/update-category-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long categoryId,

			@ModelAttribute RequestCategoryDTO requestCategoryDTO,
			@RequestPart(value = "categoryImage", required = false) MultipartFile categoryImage) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<CategoryDTO> response = categoryMgmtService.updateCategory(authUserId, categoryId,
					requestCategoryDTO, categoryImage);

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

	@DeleteMapping("/delete-category")
	public ResponseEntity<ApiResponse<CategoryDTO>> deleteCategory(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long categoryId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<CategoryDTO> response = categoryMgmtService.deleteCategory(authUserId, categoryId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/download-csv-category")
	public ResponseEntity<ApiResponse<byte[]>> downloadCategoriesCsv(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			List<CategoryDTO> categories = categoryMgmtService.getAllCategories().getContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			csvUtils.writeCategoriesToCsv(categories, outputStream);
			byte[] csvData = outputStream.toByteArray();
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			String formattedDate = currentDate.format(formatter);
			// Construct filename using the current date
			String filename = "Categories_" + formattedDate + ".csv";
			// Set the response headers
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

			// Push data inside actionLogFeignService
			ActionLogModel actionLogData = new ActionLogModel();
			actionLogData.setActionByAuthUserId(authUserId);
			actionLogData.setAuthUserId(authUserId);
			actionLogData.setActionLogMethod(ActionLogMethod.GET);
			actionLogData.setActionLogMessage("CSV downloaded successfully.");
			actionLogFeignService.addActionLog(actionLogData);

			return ResponseEntity.ok().headers(headers)
					.body(new ApiResponse<>("success", "CSV downloaded successfully.", csvData));

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>("error", "Failed to generate CSV.", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PostMapping("/upload-csv-category")
	public ResponseEntity<ApiResponse<String>> uploadCategoriesCsv(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam("file") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);
			try {
				List<RequestCategoryDTO> categories = csvUtils.readCategoriesFromCsv(file.getInputStream());
				for (RequestCategoryDTO category : categories) {
					// Check if the category already exists and update or create accordingly
					ApiResponse<CategoryDTO> response = categoryMgmtService
							.getCategoryByName(category.getCategoryName());
					if (response.getContent() != null) {
						// Update existing category
						categoryMgmtService.updateCategory(authUserId, response.getContent().getCategoryId(), category,
								file);
					} else {
						// Create new category
						categoryMgmtService.createCategory(authUserId, category, file);
					}
				}

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.POST);
				actionLogData.setActionLogMessage("Categories uploaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return ResponseEntity.ok(new ApiResponse<>("success", "Categories uploaded successfully.", null));

			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(new ApiResponse<>("error", "Error processing CSV file.", null));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
