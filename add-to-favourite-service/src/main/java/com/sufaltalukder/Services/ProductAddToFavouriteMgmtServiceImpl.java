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
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.ProductAddToFavouriteRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;
import com.sufaltalukder.feign.Services.NotificationFeignService;

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

	@Autowired
	private NotificationFeignService notificationFeignService;

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

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(userId);
		notificationData.setNotificationTitle("User Favourite Added");
		notificationData.setNotificationDescription(
				"User favourite #" + savedData.getAddToFavouriteId() + " has been added successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

		return new ApiResponse<>("success", "Add to favourite successfully.",
				ProductAddToFavouriteMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<List<ProductAddToFavouriteDTO>> getAllUserFavourites(Long userId, Long productId) {

		List<ProductAddToFavouriteModel> filteredFavourites = productAddToFavouriteRepository
				.findUsersFavouritesByFilters((userId != null && userId > 0) ? userId : null,
						(productId != null && productId > 0) ? productId : null);

		if (filteredFavourites == null || filteredFavourites.isEmpty()) {
			return new ApiResponse<>("not found", "No favourite(s) list found", null);
		}

		List<ProductAddToFavouriteDTO> dtos = filteredFavourites.stream().map(ProductAddToFavouriteMapper::toDTO)
				.toList();

		return new ApiResponse<>("success", "All favourite(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<ProductAddToFavouriteDTO> getUserFavourite(long authUserId, long addToFavouriteId, long userId) {

		ProductAddToFavouriteModel favourite = productAddToFavouriteRepository.findByIdAndUserId(addToFavouriteId,
				userId);

		if (favourite == null) {
			return new ApiResponse<>("error", "Favourite not found", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("User favourite fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "User favourite fetched successfully.",
				ProductAddToFavouriteMapper.toDTO(favourite));
	}

	@Override
	public ApiResponse<ProductAddToFavouriteDTO> updateUserFavourite(long authUserId, long addToFavouriteId,
			long userId, long productId) {

		// Fetch existing favourite
		ProductAddToFavouriteModel existingFavourite = productAddToFavouriteRepository
				.findByIdAndUserId(addToFavouriteId, userId);

		if (existingFavourite == null) {
			return new ApiResponse<>("error", "Favourite not found.", null);
		}

		// Prevent duplicate product in favourites
		long duplicateCount = productAddToFavouriteRepository.countByUserIdAndProductId(userId, productId);

		if (duplicateCount > 0 && existingFavourite.getProductInfo().getProductId() != productId) {

			return new ApiResponse<>("exist", "Product already added into favourite list!", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		ProductModel product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		existingFavourite.setAuthUserInfo(authUser);
		existingFavourite.setUserInfo(user);
		existingFavourite.setProductInfo(product);

		ProductAddToFavouriteModel savedData = productAddToFavouriteRepository.save(existingFavourite);

		// Log action
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("User favourite updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(userId);
		notificationData.setNotificationTitle("User Favourite Updated");
		notificationData.setNotificationDescription(
				"User favourite #" + savedData.getAddToFavouriteId() + " has been updated successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

		return new ApiResponse<>("success", "User favourite updated successfully.",
				ProductAddToFavouriteMapper.toDTO(savedData));
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

		// Push data inside notificationFeignService
		NotificationModel notificationData = new NotificationModel();
		notificationData.setAuthUserId(authUserId);
		notificationData.setUserId(userId);
		notificationData.setNotificationTitle("User Favourite Removed");
		notificationData
				.setNotificationDescription("User favourite #" + addToFavouriteId + " has been removed successfully.");
		notificationFeignService.pushMgmtNotification(notificationData);

		return new ApiResponse<>("success", "Favourite removed successfully.", null);
	}
}