package com.sufaltalukder.Controllers;

import java.io.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Services.SubCategoryMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;
import com.sufaltalukder.Utils.ExcelUtils;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class SubCategoryMgmtController {

	@Autowired
	private SubCategoryMgmtService subCategoryMgmtService;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@Autowired
	private ExcelUtils excelUtils;

	@PostMapping("/create-subcategory")
	public ResponseEntity<ApiResponse<SubCategoryDTO>> createSubCategory(@RequestHeader("authToken") String authToken,
			@RequestBody SubCategoryModel subCategoryModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			subCategoryModel.setAuthUserId(authUserId);

			ApiResponse<SubCategoryDTO> response = subCategoryMgmtService.createSubCategory(subCategoryModel);

			if ("exist".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-subcategory")
	public ResponseEntity<ApiResponse<SubCategoryDTO>> getSubCategory(@RequestHeader("authToken") String authToken,
			@RequestParam long subCategoryId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<SubCategoryDTO> response = subCategoryMgmtService.getSubCategory(authUserId, subCategoryId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-subcategory")
	public ResponseEntity<ApiResponse<List<SubCategoryDTO>>> getAllSubCategories(
			@RequestHeader("authToken") String authToken) {
		try {
			authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<List<SubCategoryDTO>> response = subCategoryMgmtService.getAllSubCategories();

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping("/update-subcategory-details")
	public ResponseEntity<ApiResponse<SubCategoryDTO>> updateSubCategory(@RequestHeader("authToken") String authToken,
			@RequestParam long subCategoryId, @RequestBody SubCategoryModel subCategoryModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			subCategoryModel.setAuthUserId(authUserId);

			ApiResponse<SubCategoryDTO> response = subCategoryMgmtService.updateSubCategory(subCategoryId,
					subCategoryModel);

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

	@DeleteMapping("/delete-subcategory")
	public ResponseEntity<ApiResponse<SubCategoryDTO>> deleteSubCategory(@RequestHeader("authToken") String authToken,
			@RequestParam long subCategoryId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);

			ApiResponse<SubCategoryDTO> response = subCategoryMgmtService.deleteSubCategory(authUserId, subCategoryId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/download-subcategory-excel")
	public ResponseEntity<ApiResponse<byte[]>> downloadSubCategoriesExcel(
			@RequestHeader("authToken") String authToken) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			List<SubCategoryDTO> subCategories = subCategoryMgmtService.getAllSubCategories().getContent();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				excelUtils.writeSubCategoriesToExcel(subCategories, outputStream);
				byte[] excelData = outputStream.toByteArray();
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=subcategories.xlsx");

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.GET);
				actionLogData.setActionLogMessage("Excel downloaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return ResponseEntity.ok().headers(headers)
						.body(new ApiResponse<>("success", "Excel downloaded successfully.", excelData));
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PostMapping("/upload-subcategory-excel")
	public ResponseEntity<ApiResponse<String>> uploadSubCategoriesExcel(@RequestHeader("authToken") String authToken,
			@RequestParam("file") MultipartFile file) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			try {
				List<SubCategoryModel> subCategories = excelUtils.readSubCategoriesFromExcel(file.getInputStream());
				for (SubCategoryModel subCategory : subCategories) {
					// Check if the subCategory already exists based on its ID
					if (subCategory.getSubCategoryId() > 0) {
						// Attempt to get the existing subCategory
						ApiResponse<SubCategoryDTO> response = subCategoryMgmtService.getSubCategory(authUserId,
								subCategory.getSubCategoryId());
						if ("success".equals(response.getStatus())) {
							// Update existing subCategory
							subCategoryMgmtService.updateSubCategory(subCategory.getSubCategoryId(), subCategory);
						} else {
							// If not found, create a new subCategory
							subCategoryMgmtService.createSubCategory(subCategory);
						}
					} else {
						// If no ID is provided, create a new subCategory
						subCategoryMgmtService.createSubCategory(subCategory);
					}
				}

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.GET);
				actionLogData.setActionLogMessage("Excel subcategorie(s) uploaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return ResponseEntity
						.ok(new ApiResponse<>("success", "Excel subcategorie(s) uploaded successfully.", null));
			} catch (IOException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new ApiResponse<>("error", e.getMessage(), null));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}
}
