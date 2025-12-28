package com.sufaltalukder.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Mappers.ProductAddToFavouriteMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.ProductAddToFavouriteRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;

@Service
public class ProductAddToFavouriteServiceImpl implements ProductAddToFavouriteService {

	@Autowired
	private ProductAddToFavouriteRepository productAddToFavouriteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ApiResponse<ProductAddToFavouriteDTO> createUserFavourite(long userId, long productId,
			ProductAddToFavouriteModel productAddToFavouriteModel) {

		Optional<UserModel> user = userRepository.findByUserId(userId);
		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		Optional<ProductModel> product = productRepository.findByProductId(productId);
		if (product.isEmpty()) {
			return new ApiResponse<>("not found", "Product not found.", null);
		}

		long favouritesCount = productAddToFavouriteRepository.findCustomerByProductId(productId, userId);
		if (favouritesCount > 0) {
			return new ApiResponse<>("exist", "Product already added into favourite list!", null);
		}

		ProductAddToFavouriteModel savedData = productAddToFavouriteRepository.save(productAddToFavouriteModel);
		return new ApiResponse<>("success", "Add to favourite successfully.",
				ProductAddToFavouriteMapper.toDTO(savedData));
	}

	@Override
	public PaginationApiResponse<List<ProductAddToFavouriteDTO>> getUserFavourites(long userId, int pageNo,
			int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<ProductAddToFavouriteModel> filteredFavourites = productAddToFavouriteRepository.findByUserId(userId,
				pageable);

		if (filteredFavourites.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No favourite(s) list found for user ID: " + userId, null,
					0, 0, 0);
		}

		List<ProductAddToFavouriteDTO> dtos = filteredFavourites.stream().map(ProductAddToFavouriteMapper::toDTO)
				.toList();

		return new PaginationApiResponse<>("success", "User favourite(s) fetched successfully.", dtos,
				filteredFavourites.getNumber() + 1, filteredFavourites.getSize(),
				filteredFavourites.getTotalElements());
	}

	@Override
	public ApiResponse<Void> removeUserFavourite(long addToFavouriteId, long userId) {

		Optional<ProductAddToFavouriteModel> favourite = productAddToFavouriteRepository.findById(addToFavouriteId);
		if (favourite.isEmpty()) {
			return new ApiResponse<>("not found", "Favourite ID not found.", null);
		}

		Optional<UserModel> user = userRepository.findByUserId(userId);
		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		if (favourite.get().getUserInfo().getUserId() != userId) {
			return new ApiResponse<>("not applicable", "This user doesn't own this favourite ID.", null);
		}

		productAddToFavouriteRepository.deleteById(addToFavouriteId);
		return new ApiResponse<>("success", "Favourite removed successfully.", null);
	}
}
