package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;

public interface ProductAddToFavouriteMgmtService {

	ApiResponse<ProductAddToFavouriteDTO> createUserFavourite(ProductAddToFavouriteModel productAddToFavouriteModel);

	PaginationApiResponse<List<ProductAddToFavouriteDTO>> getUserFavourites(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	ApiResponse<Void> removeUserFavourite(long addToFavouriteId, long userId);

}
