package com.sufaltalukder.Services;

import java.util.*;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;

public interface ProductAddToCartMgmtService {

	ApiResponse<ProductAddToCartDTO> addUserCart(long authUserId, long userId, long productId,
			ProductAddToCartModel productAddToCartModel);

	ApiResponse<List<ProductAddToCartDTO>> getAllCarts(long authUserId, long userId);

	ApiResponse<Void> removeUserCart(long authUserId, long addToCartId, long userId);

	ApiResponse<ProductAddToCartModel> getUserCart(long addToCartId, long userId);

	Double getPriceOfSelectedProduct(long productId);

}
