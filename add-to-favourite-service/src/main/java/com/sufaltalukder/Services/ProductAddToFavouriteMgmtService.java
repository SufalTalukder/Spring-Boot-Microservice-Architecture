package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface ProductAddToFavouriteMgmtService {

	ApiResponse<ProductAddToFavouriteDTO> createUserFavourite(long authUserId, long userId, long productId);

	ApiResponse<List<ProductAddToFavouriteDTO>> getAllUserFavourites(Long userId, Long productId);

	ApiResponse<ProductAddToFavouriteDTO> getUserFavourite(long authUserId, long addToFavouriteId, long userId);

	ApiResponse<ProductAddToFavouriteDTO> updateUserFavourite(long authUserId, long addToFavouriteId, long userId,
			long productId);

	ApiResponse<Void> removeUserFavourite(long authUserId, long addToFavouriteId, long userId);

}
