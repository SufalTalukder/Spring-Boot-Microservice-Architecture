package com.sufaltalukder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.DTOs.RequestProductDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Services.ProductMgmtService;
import com.sufaltalukder.Utils.AuthJwtUtil;

@RestController
@RequestMapping("/api/v1/elastic/auth")
public class ProductMgmtController {

	@Autowired
	private ProductMgmtService productMgmtService;

	@Autowired
	private AuthJwtUtil authJwtUtil;

	@PostMapping(value = "/create-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam(value = "categoryId", required = false) long categoryId,
			@RequestParam(value = "subCategoryId", required = false) long subCategoryId,
			@RequestParam(value = "languageId", required = false) long languageId,

			@ModelAttribute RequestProductDTO productInfo,
			@RequestPart(value = "productImage", required = false) MultipartFile productImage) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<ProductDTO> response = productMgmtService.createProduct(authUserId, categoryId, subCategoryId,
					languageId, productInfo, productImage);

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

	@PostMapping("/multi-product-create")
	public ResponseEntity<ApiResponse<List<ProductDTO>>> createMultipleProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestBody List<ProductModel> productModels) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<ProductDTO>> response = productMgmtService.createMultipleProduct(authUserId,
					productModels);

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

	@GetMapping("/get-product")
	public ResponseEntity<ApiResponse<ProductDTO>> getProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long productId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<ProductDTO> response = productMgmtService.getProduct(authUserId, productId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/get-all-products")
	public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret,
			@RequestParam(value = "categoryId", required = false) long categoryId,
			@RequestParam(value = "subCategoryId", required = false) long subCategoryId,
			@RequestParam(value = "languageId", required = false) long languageId) {
		try {
			authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<List<ProductDTO>> response = productMgmtService.getAllProducts(categoryId, subCategoryId,
					languageId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@PutMapping(value = "/update-product-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long productId,
			@RequestParam(value = "categoryId", required = false) long categoryId,
			@RequestParam(value = "subCategoryId", required = false) long subCategoryId,
			@RequestParam(value = "languageId", required = false) long languageId,

			@ModelAttribute RequestProductDTO productInfo,
			@RequestPart(value = "productImage", required = false) MultipartFile productImage) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<ProductDTO> response = productMgmtService.updateProduct(authUserId, productId, categoryId,
					subCategoryId, languageId, productInfo, productImage);

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

	@DeleteMapping("/delete-product")
	public ResponseEntity<ApiResponse<ProductDTO>> deleteProduct(
			@RequestHeader(value = "authToken", required = false) String authToken,
			@RequestHeader(value = "x-api-key", required = false) String apiKey,
			@RequestHeader(value = "x-api-secret", required = false) String apiSecret, @RequestParam long productId) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken, apiKey, apiSecret);

			ApiResponse<ProductDTO> response = productMgmtService.deleteProduct(authUserId, productId);

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