package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Mappers.ProductAddToCartMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CartApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.ProductAddToCartRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;

@Service
public class ProductAddToCartMgmtServiceImpl implements ProductAddToCartMgmtService {

	@Autowired
	private ProductAddToCartRepository productAddToCartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ApiResponse<ProductAddToCartDTO> createUserCart(ProductAddToCartModel productAddToCartModel) {

		Optional<UserModel> user = userRepository.findByUserId(productAddToCartModel.getUserId());
		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		Optional<ProductModel> product = productRepository.findByProductId(productAddToCartModel.getProductId());
		if (product.isEmpty()) {
			return new ApiResponse<>("not found", "Product not found.", null);
		}

		long cartsCount = productAddToCartRepository.findUserByProductId(productAddToCartModel.getProductId(),
				productAddToCartModel.getUserId());
		if (cartsCount > 0) {
			return new ApiResponse<>("exist", "Product already added into cart list!", null);
		}

		ProductAddToCartModel savedData = productAddToCartRepository.save(productAddToCartModel);
		return new ApiResponse<>("success", "Add to cart list successfully.", ProductAddToCartMapper.toDTO(savedData));
	}

	@Override
	public PaginationApiResponse<List<ProductAddToCartDTO>> getUserCarts(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<ProductAddToCartModel> filteredCarts = productAddToCartRepository.findByUserId(userId, pageable);

		if (filteredCarts.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No cart(s) list found for user ID: " + userId, null, 0, 0,
					0);
		}

		List<ProductAddToCartDTO> dtos = filteredCarts.stream().map(ProductAddToCartMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "User carts fetched successfully.", dtos,
				filteredCarts.getNumber() + 1, filteredCarts.getSize(), filteredCarts.getTotalElements());
	}

	@Override
	public CartApiResponse<List<ProductAddToCartDTO>> updateUserMultiCarts(List<ProductAddToCartModel> cartUpdateModels,
			long userId) {

		List<ProductAddToCartDTO> updatedCarts = new ArrayList<>();
		double totalCartPrice = 0;

		for (ProductAddToCartModel updateModel : cartUpdateModels) {
			long cartId = updateModel.getAddToCartId();
			long productId = updateModel.getProductId();

			Optional<ProductAddToCartModel> optionalCart = productAddToCartRepository.findById(cartId);

			if (optionalCart.isEmpty()) {
				return new CartApiResponse<>("not found", "Cart ID: " + cartId + " not found.", null, 0);
			}

			ProductAddToCartModel existingCart = optionalCart.get();

			// Validate userId match
			if (existingCart.getUserId() != userId) {
				return new CartApiResponse<>("not found",
						"Cart ID: " + cartId + " does not belong to user ID: " + userId, null, 0);
			}

			// Validate productId match
			if (existingCart.getProductId() != productId) {
				return new CartApiResponse<>("not found",
						"Product ID: " + productId + " not found in user's cart (Cart ID: " + cartId + ")", null, 0);
			}

			// Fetch each product price
			ProductModel product = productRepository.findById(productId).orElse(null);
			if (product == null) {
				return new CartApiResponse<>("not found", "Product ID: " + productId + " not found.", null, 0);
			}

			int quantity = updateModel.getQuantity();
			double itemTotal = product.getProductPrice() * quantity;
			existingCart.setQuantity(quantity);
			existingCart.setEachProductTotalPrice(product.getProductPrice() * quantity);
			existingCart.setCartUpdatedAt(ZonedDateTime.now());

			productAddToCartRepository.save(existingCart);

			totalCartPrice += itemTotal;

			updatedCarts.add(ProductAddToCartMapper.toDTO(existingCart));
		}

		return new CartApiResponse<>("success", "Cart(s) updated successfully.", updatedCarts, totalCartPrice);
	}

	@Override
	public ApiResponse<Void> removeUserCart(long addToCartId, long userId) {

		Optional<ProductAddToCartModel> cart = productAddToCartRepository.findById(addToCartId);
		if (cart.isEmpty()) {
			return new ApiResponse<>("not found", "Cart ID not found.", null);
		}

		Optional<UserModel> user = userRepository.findByUserId(userId);
		if (user.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		if (cart.get().getUserId() != userId) {
			return new ApiResponse<>("not applicable", "This user doesn't own this cart ID.", null);
		}

		productAddToCartRepository.deleteById(addToCartId);
		return new ApiResponse<>("success", "Cart removed successfully.", null);
	}

	@Override
	public ApiResponse<Void> removeUserAllCarts(String addToCartIds, long userId) {
		List<Long> notFoundCartIds = new ArrayList<>();

		// Convert comma-separated string to List<Long>
		List<Long> cartIdList = Arrays.stream(addToCartIds.split(",")).map(String::trim).filter(s -> !s.isEmpty())
				.map(Long::parseLong).toList();

		for (Long eachId : cartIdList) {
			Optional<ProductAddToCartModel> optionalCart = productAddToCartRepository.findById(eachId);

			if (optionalCart.isPresent()) {
				ProductAddToCartModel cart = optionalCart.get();
				if (cart.getUserId() == userId) {
					productAddToCartRepository.deleteById(eachId);
				} else {
					notFoundCartIds.add(eachId);
				}
			} else {
				notFoundCartIds.add(eachId);
			}
		}

		if (!notFoundCartIds.isEmpty()) {
			return new ApiResponse<>("not found", "The cart ID(s) were not found or do not belong to user ID: "
					+ userId + ": " + notFoundCartIds, null);
		}

		return new ApiResponse<>("success", "Cart(s) removed successfully.", null);
	}
}
