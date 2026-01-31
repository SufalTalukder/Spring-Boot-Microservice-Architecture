package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.DTOs.RequestProductDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductModel;

public interface ProductMgmtService {

	ApiResponse<ProductDTO> createProduct(long authUserId, long categoryId, long subCategoryId, long languageId,
			RequestProductDTO productInfo, MultipartFile productImage);

	ApiResponse<List<ProductDTO>> createMultipleProduct(long authUserId, List<ProductModel> productModels);

	ApiResponse<ProductDTO> getProduct(long authUserId, long productId);

	ApiResponse<List<ProductDTO>> getAllProducts(long categoryId, long subCategoryId, long languageId);

	ApiResponse<ProductDTO> updateProduct(long authUserId, long productId, long categoryId, long subCategoryId,
			long languageId, RequestProductDTO productInfo, MultipartFile productImage);

	ApiResponse<ProductDTO> deleteProduct(long authUserId, long productId);

}
