package com.sufaltalukder.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Mappers.ProductAddToCartMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.ProductAddToCartRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class ProductAddToCartMgmtServiceImpl implements ProductAddToCartMgmtService {

	@Autowired
	private ProductAddToCartRepository productAddToCartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // Via feign client

	@Override
	public ApiResponse<ProductAddToCartDTO> addUserCart(long authUserId, long userId, long productId,
			ProductAddToCartModel productAddToCartModel) {

		long cartCount = productAddToCartRepository.existsByUserIdAndProductId(userId, productId);

		if (cartCount > 0) {
			return new ApiResponse<>("exist", "Product already exists for this user.", null);
		}

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		ProductModel product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		ProductAddToCartModel savingData = new ProductAddToCartModel();
		savingData.setAuthUserInfo(authUser);
		savingData.setUserInfo(user);
		savingData.setProductInfo(product);
		savingData.setQuantity(productAddToCartModel.getQuantity());
		savingData.setEachProductTotalPrice(productAddToCartModel.getEachProductTotalPrice());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Cart added successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		ProductAddToCartModel savedData = productAddToCartRepository.save(savingData);

		return new ApiResponse<>("success", "Cart added successfully.", ProductAddToCartMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<List<ProductAddToCartDTO>> getAllCarts(long authUserId, long userId) {

		List<ProductAddToCartModel> carts;

		if (userId > 0) {
			carts = productAddToCartRepository.findUserCartsByUserId(userId);
		} else {
			carts = productAddToCartRepository.findAllCarts();
		}

		if (carts == null || carts.isEmpty()) {

			String message = (userId > 0) ? "No cart(s) list found for this user." : "No cart(s) list found.";

			return new ApiResponse<>("not found", message, null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Cart(s) fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		List<ProductAddToCartDTO> dtos = carts.stream().map(ProductAddToCartMapper::toDTO).toList();

		return new ApiResponse<>("success", "Cart(s) fetched successfully.", dtos);
	}

	@Override
	public Double getPriceOfSelectedProduct(long productId) {
		return productRepository.findProductPriceByProductId(productId);
	}

	@Override
	public ApiResponse<Void> removeUserCart(long authUserId, long addToCartId, long userId) {

		Optional<ProductAddToCartModel> cart = productAddToCartRepository.findById(addToCartId);

		if (cart.isEmpty()) {
			return new ApiResponse<>("not found", "Cart ID not found.", null);
		}

		Optional<UserModel> user = userRepository.findByUserId(userId);

		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		if (cart.get().getUserInfo().getUserId() != userId) {
			return new ApiResponse<>("not applicable", "This user doesn't own this cart ID.", null);
		}

		productAddToCartRepository.deleteById(addToCartId);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Cart removed successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Cart removed successfully.", null);
	}

	@Override
	public ApiResponse<ProductAddToCartModel> getUserCart(long addToCartId, long userId) {

		ProductAddToCartModel cart = productAddToCartRepository.findCartByUserId(addToCartId, userId);

		if (cart == null) {
			return new ApiResponse<>("not applicable", "This user doesn't own this cart ID.", null);
		}

		return new ApiResponse<>("success", "Cart ID fetched successfully.", cart);
	}
}
