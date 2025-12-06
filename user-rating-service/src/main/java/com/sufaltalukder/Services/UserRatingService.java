package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.UserRatingDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserRatingModel;

public interface UserRatingService {

	ApiResponse<UserRatingDTO> addUserRating(UserRatingModel userRatingModel);

	PaginationApiResponse<List<UserRatingDTO>> getProductAllRatings(long productId, int pageNo, int pageSize);

	ApiResponse<UserRatingDTO> updateUserRating(long userRatingId, UserRatingModel userRatingModel);

	ApiResponse<UserRatingDTO> deleteUserRating(long userId, long productId, long userRatingId);

}
