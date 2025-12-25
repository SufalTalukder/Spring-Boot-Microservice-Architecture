package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductModel;

public interface ProductMgmtService {

	ApiResponse<ProductDTO> createProduct(long authUserId, long categoryId, long subCategoryId, long languageId,
			ProductModel productModel);

	ApiResponse<List<ProductDTO>> createMultipleProduct(long authUserId, List<ProductModel> productModels);

	ApiResponse<ProductDTO> getProduct(long authUserId, long productId);

	ApiResponse<List<ProductDTO>> getAllProducts();

	ApiResponse<ProductDTO> updateProduct(long authUserId, long productId, long categoryId, long subCategoryId,
			long languageId, ProductModel productModel);

	ApiResponse<ProductDTO> deleteProduct(long authUserId, long productId);

	ApiResponse<List<ProductDTO>> getSearchedResults(String q);

	ApiResponse<List<ProductDTO>> getAllProductsFilterByLanguage(long languageId);

	ApiResponse<List<ProductDTO>> getAllProductsFilterByCategory(long categoryId);

	ApiResponse<List<ProductDTO>> getAllProductsFilterBySubCategory(long subCategoryId);

}
