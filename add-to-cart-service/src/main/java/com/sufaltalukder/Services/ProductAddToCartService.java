package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CartApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;

public interface ProductAddToCartService {

	ApiResponse<ProductAddToCartDTO> createUserCart(ProductAddToCartModel productAddToCartModel);

	PaginationApiResponse<List<ProductAddToCartDTO>> getUserCarts(long userId, int pageNo, int pageSize, String sortBy,
			String sortDir);

	CartApiResponse<List<ProductAddToCartDTO>> updateUserMultiCarts(List<ProductAddToCartModel> cartUpdateModels,
			long userId);

	ApiResponse<Void> removeUserCart(long addToCartId, long userId);
	
	ApiResponse<Void> removeUserAllCarts(String addToCartIds, long userId);

	ApiResponse<ProductAddToCartModel> getUserCart(long addToCartId, long userId);

}
