package com.sufaltalukder.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.Mappers.CheckOutHistoryMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.CheckOutHistoryModel.OrderStatus;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Repositories.CheckOutHistoryRepository;
import com.sufaltalukder.Repositories.ProductAddToCartRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.AddToCartFeignService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerPurchaseServiceImpl implements CustomerPurchaseService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddToCartFeignService addToCartFeignService; // addToCart feign service

	@Autowired
	private CheckOutHistoryRepository checkOutHistoryRepository;

	@Autowired
	private ProductAddToCartRepository productAddToCartRepository;

	@Override
	public ApiResponse<CheckOutDTO> createUserCheckOut(long userId, CheckOutHistoryModel checkOutHistoryModel) {

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		// System.out.println("User ID: " + userId);

		double totalPaymentAmount = 0;
		StringBuilder cartIdsBuilder = new StringBuilder();
		StringBuilder productIdsBuilder = new StringBuilder();

		for (String cartIdStr : checkOutHistoryModel.getAddToCartIds().split(",")) {

			if (cartIdStr.isBlank())
				continue;

			long eachCartId = Long.parseLong(cartIdStr.trim());

			// call addToCart feign
			ApiResponse<ProductAddToCartModel> apiResponse = addToCartFeignService.getUserCart(eachCartId, userId);

			if (apiResponse == null || !"success".equals(apiResponse.getStatus())) {
				return new ApiResponse<>("not found",
						"Cart ID: " + eachCartId + " not found for User ID: " + user.getUserId(), null);
			}

			// fetch productId by cardId
			long findEachProductIdByCartId = productAddToCartRepository.findProductByCartId(eachCartId);

			if (findEachProductIdByCartId == 0) {
				return new ApiResponse<>("not found", "Cart ID not found.", null);
			}

			ProductAddToCartModel cart = apiResponse.getContent();

			totalPaymentAmount += cart.getEachProductTotalPrice();
			cartIdsBuilder.append(eachCartId).append(",");
			productIdsBuilder.append(findEachProductIdByCartId).append(",");
		}

		String cartIds = cartIdsBuilder.length() > 0 ? cartIdsBuilder.substring(0, cartIdsBuilder.length() - 1) : "";
		String productIds = productIdsBuilder.length() > 0
				? productIdsBuilder.substring(0, productIdsBuilder.length() - 1)
				: "";

		CheckOutHistoryModel addData = new CheckOutHistoryModel();

		addData.setAuthUserInfo(null);
		addData.setUserInfo(user);
		addData.setAddToCartIds(cartIds);
		addData.setProductIds(productIds);

		addData.setPaymentAddress(checkOutHistoryModel.getPaymentAddress());
		addData.setShippingAddress(checkOutHistoryModel.getShippingAddress());
		addData.setShippingMethod(checkOutHistoryModel.getShippingMethod());
		addData.setPaymentMethod(checkOutHistoryModel.getPaymentMethod());
		addData.setPaymentAmount(totalPaymentAmount);
		addData.setDeliveryInDays(checkOutHistoryModel.getDeliveryInDays());
		addData.setOrderStatus(OrderStatus.SUCCESSFUL);

		if (addData.getPaymentMethod() == PaymentMethod.COD) {
			addData.setPaymentStatus("PAYMENT_PENDING");
		} else {
			addData.setPaymentStatus("PAYMENT_SUCCESS");
		}

		CheckOutHistoryModel saved = checkOutHistoryRepository.save(addData);

		return new ApiResponse<>("success", "Order checkout successfully.", CheckOutHistoryMapper.toDto(saved));
	}

	@Override
	public ApiResponse<CheckOutDTO> getPurchaseDetails(long userId, long checkOutHistoryId) {

		CheckOutHistoryModel data = checkOutHistoryRepository.findByCheckOutHistoryIdAndUserId(checkOutHistoryId,
				userId);

		if (data == null) {
			return new ApiResponse<>("not found",
					"Order history ID: " + checkOutHistoryId + " not found for User ID: " + userId, null);
		}

		return new ApiResponse<>("success", "Order details fetched successfully.", CheckOutHistoryMapper.toDto(data));
	}

	@Override
	public PaginationApiResponse<List<CheckOutDTO>> getAllPurchasesList(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<CheckOutHistoryModel> page = checkOutHistoryRepository.findCheckoutHistoriesOfUser(userId, pageable);

		if (page.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No order(s) found for user ID: " + userId, null, 0, 0, 0);
		}

		List<CheckOutDTO> dtos = page.stream().map(CheckOutHistoryMapper::toDto).toList();

		return new PaginationApiResponse<>("success", "Order list fetched.", dtos, page.getNumber() + 1,
				page.getTotalPages(), page.getTotalElements());
	}

	@Override
	public ApiResponse<CheckOutDTO> cancelPurchase(long userId, long checkOutHistoryId) {

		CheckOutHistoryModel data = checkOutHistoryRepository.findByCheckOutHistoryIdAndUserId(checkOutHistoryId,
				userId);

		if (data == null) {
			return new ApiResponse<>("not found",
					"Order history ID: " + checkOutHistoryId + " not found for User ID: " + userId, null);
		}

		data.setOrderStatus(OrderStatus.CANCELLED);
		data.setPaymentStatus("PAYMENT_RETURN_PENDING");

		CheckOutHistoryModel saved = checkOutHistoryRepository.save(data);

		return new ApiResponse<>("success", "Order cancelled successfully.", CheckOutHistoryMapper.toDto(saved));
	}
}