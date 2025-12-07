package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductModel;

public interface ProductMgmtService {

	ApiResponse<ProductDTO> createProduct(ProductModel productModel);

	ApiResponse<List<ProductDTO>> createMultipleProduct(List<ProductModel> productModels);

	ApiResponse<ProductDTO> getProduct(long productId);

	PaginationApiResponse<List<ProductDTO>> getAllProducts(int pageNo, int pageSize);

	ApiResponse<ProductDTO> updateProduct(long productId, ProductModel productModel);

	ApiResponse<ProductDTO> deleteProduct(long productId);

	ApiResponse<List<ProductDTO>> getSearchedResults(String q);

	PaginationApiResponse<List<ProductDTO>> getAllProductsFilterByLanguage(long languageId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	PaginationApiResponse<List<ProductDTO>> getAllProductsFilterByCategory(long categoryId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	PaginationApiResponse<List<ProductDTO>> getAllProductsFilterBySubCategory(long subCategoryId, int pageNo,
			int pageSize, String sortBy, String sortDir);

}
