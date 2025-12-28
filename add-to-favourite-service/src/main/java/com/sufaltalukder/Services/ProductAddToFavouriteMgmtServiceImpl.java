package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Mappers.ProductAddToFavouriteMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.ProductAddToFavouriteRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class ProductAddToFavouriteMgmtServiceImpl implements ProductAddToFavouriteMgmtService {

	@Autowired
	private ProductAddToFavouriteRepository productAddToFavouriteRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // Via feign client

	@Override
	public ApiResponse<ProductAddToFavouriteDTO> createUserFavourite(long authUserId, long userId, long productId) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		ProductModel product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		long favouritesCount = productAddToFavouriteRepository.findCustomerByProductId(productId, userId);

		if (favouritesCount > 0) {
			return new ApiResponse<>("exist", "Product already added into favourite list of this user!", null);
		}

		ProductAddToFavouriteModel savingData = new ProductAddToFavouriteModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setUserInfo(user);
		savingData.setProductInfo(product);
		savingData.setFavouriteCreatedAt(ZonedDateTime.now());

		ProductAddToFavouriteModel savedData = productAddToFavouriteRepository.save(savingData);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Add to favourite successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Add to favourite successfully.",
				ProductAddToFavouriteMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<List<ProductAddToFavouriteDTO>> getUserFavourites() {

		List<ProductAddToFavouriteModel> filteredFavourites = productAddToFavouriteRepository
				.findUsersFavouritesByAuth();

		if (filteredFavourites.isEmpty()) {
			return new ApiResponse<>("not found", "No favourite(s) list found", null);
		}

		List<ProductAddToFavouriteDTO> dtos = filteredFavourites.stream().map(ProductAddToFavouriteMapper::toDTO)
				.toList();

		return new ApiResponse<>("success", "Products fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<Void> removeUserFavourite(long authUserId, long addToFavouriteId, long userId) {

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

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Favourite removed successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Favourite removed successfully.", null);
	}
}