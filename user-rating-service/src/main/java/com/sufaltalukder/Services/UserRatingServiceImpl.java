package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.UserRatingDTO;
import com.sufaltalukder.Mappers.UserRatingMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserRatingModel;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRatingRepository;
import com.sufaltalukder.Repositories.UserRepository;

@Service
public class UserRatingServiceImpl implements UserRatingService {

	@Autowired
	private UserRatingRepository userRatingRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ApiResponse<UserRatingDTO> addUserRating(UserRatingModel userRatingModel) {

		if (userRepository.findByUserId(userRatingModel.getUserId()).isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		if (productRepository.findByProductId(userRatingModel.getProductId()).isEmpty()) {
			return new ApiResponse<>("not found", "Product not found.", null);
		}

		UserRatingModel saved = userRatingRepository.save(userRatingModel);

		return new ApiResponse<>("success", "User rating added successfully.", UserRatingMapper.toDTO(saved));
	}

	@Override
	public PaginationApiResponse<List<UserRatingDTO>> getProductAllRatings(long productId, int pageNo, int pageSize) {

		Page<UserRatingModel> ratings = userRatingRepository.findAllRatingsOfProduct(productId,
				PageRequest.of(pageNo - 1, pageSize));

		if (ratings.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No ratings found.", null, 0, 0, 0);
		}

		List<UserRatingDTO> dtos = ratings.map(UserRatingMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Ratings fetched successfully.", dtos, ratings.getNumber() + 1,
				ratings.getSize(), ratings.getTotalElements());
	}

	@Override
	public ApiResponse<UserRatingDTO> updateUserRating(long userRatingId, UserRatingModel userRatingModel) {

		if (userRepository.findByUserId(userRatingModel.getUserId()).isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		if (productRepository.findByProductId(userRatingModel.getProductId()).isEmpty()) {
			return new ApiResponse<>("not found", "Product not found.", null);
		}

		UserRatingModel existing = userRatingRepository.findUserRatingOfProductByUserId(userRatingModel.getUserId(),
				userRatingModel.getProductId(), userRatingId);

		if (existing == null) {
			return new ApiResponse<>("not found", "Rating ID does not belong to this user or, product.", null);
		}

		existing.setProductId(userRatingModel.getProductId());
		existing.setUserRating(userRatingModel.getUserRating());
		existing.setUserComment(userRatingModel.getUserComment());
		existing.setUserRatingUpdatedAt(ZonedDateTime.now());

		UserRatingModel saved = userRatingRepository.save(existing);

		return new ApiResponse<>("success", "User rating updated successfully.", UserRatingMapper.toDTO(saved));
	}

	@Override
	public ApiResponse<UserRatingDTO> deleteUserRating(long userId, long productId, long userRatingId) {

		UserRatingModel exists = userRatingRepository.findByUserRatingId(userRatingId);

		if (exists == null) {
			return new ApiResponse<>("not found", "Rating ID not found.", null);
		}

		UserRatingModel valid = userRatingRepository.findUserRatingOfProductByUserId(userId, productId, userRatingId);

		if (valid == null) {
			return new ApiResponse<>("not found", "Rating ID does not belong to this user or, product.", null);
		}

		userRatingRepository.deleteById(userRatingId);

		return new ApiResponse<>("success", "Rating deleted successfully.", null);
	}
}