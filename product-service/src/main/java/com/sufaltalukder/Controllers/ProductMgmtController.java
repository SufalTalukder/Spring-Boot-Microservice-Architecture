package com.sufaltalukder.Controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
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

	@PostMapping("/create-product")
	public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestHeader String authToken,
			@RequestBody ProductModel productModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			productModel.setAuthUserId(authUserId);

			ApiResponse<ProductDTO> response = productMgmtService.createProduct(productModel);

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
	public ResponseEntity<ApiResponse<List<ProductDTO>>> createMultipleProduct(@RequestHeader String authToken,
			@RequestBody List<ProductModel> productModels) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			for (ProductModel productModel : productModels) {
				productModel.setAuthUserId(authUserId);
			}

			ApiResponse<List<ProductDTO>> response = productMgmtService.createMultipleProduct(productModels);

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
	public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@RequestHeader String authToken,
			@RequestParam long productId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<ProductDTO> response = productMgmtService.getProduct(productId);

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
	public ResponseEntity<PaginationApiResponse<List<ProductDTO>>> getAllProducts(@RequestHeader String authToken,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<ProductDTO>> response = productMgmtService.getAllProducts(pageNo, pageSize);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	@PutMapping("/update-product-details")
	public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(@RequestHeader String authToken,
			@RequestParam long productId, @RequestBody ProductModel productModel) {
		try {
			long authUserId = authJwtUtil.extractAuthUserId(authToken);
			productModel.setAuthUserId(authUserId);

			ApiResponse<ProductDTO> response = productMgmtService.updateProduct(productId, productModel);

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
	public ResponseEntity<ApiResponse<ProductDTO>> deleteProduct(@RequestHeader String authToken,
			@RequestParam long productId) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<ProductDTO> response = productMgmtService.deleteProduct(productId);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	@GetMapping("/search-product")
	public ResponseEntity<ApiResponse<List<ProductDTO>>> getSearchedResults(@RequestHeader String authToken,
			@RequestParam String q) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			ApiResponse<List<ProductDTO>> response = productMgmtService.getSearchedResults(q);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse<>("error", "Unauthorized access.", null));
		}
	}

	// Filter by Language
	@GetMapping("/get-all-products-by-language/{languageId}")
	public ResponseEntity<PaginationApiResponse<List<ProductDTO>>> getAllProductsFilterByLanguage(
			@RequestHeader("authToken") String authToken, @PathVariable long languageId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "productName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<ProductDTO>> response = productMgmtService
					.getAllProductsFilterByLanguage(languageId, pageNo, pageSize, sortBy, sortDir);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	// Filter by Category
	@GetMapping("/get-all-products-by-category/{categoryId}")
	public ResponseEntity<PaginationApiResponse<List<ProductDTO>>> getAllProductsFilterByCategory(
			@RequestHeader("authToken") String authToken, @PathVariable long categoryId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "productName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<ProductDTO>> response = productMgmtService
					.getAllProductsFilterByCategory(categoryId, pageNo, pageSize, sortBy, sortDir);

			if ("not found".equals(response.getStatus())) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new PaginationApiResponse<>("error", "Unauthorized access.", null, 0, 0, 0));
		}
	}

	// Filter by SubCategory
	@GetMapping("/read-all-products-by-subcategory/{subCategoryId}")
	public ResponseEntity<PaginationApiResponse<List<ProductDTO>>> getAllProductsFilterBySubCategory(
			@RequestHeader("authToken") String authToken, @PathVariable long subCategoryId,
			@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "productName") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDir) {
		try {
			authJwtUtil.extractAuthUserId(authToken);
			PaginationApiResponse<List<ProductDTO>> response = productMgmtService
					.getAllProductsFilterBySubCategory(subCategoryId, pageNo, pageSize, sortBy, sortDir);

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