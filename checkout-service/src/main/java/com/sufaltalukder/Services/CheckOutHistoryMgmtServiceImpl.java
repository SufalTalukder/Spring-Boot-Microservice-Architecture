package com.sufaltalukder.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryRequest;
import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Mappers.CheckOutHistoryMapper;
import com.sufaltalukder.Mappers.ProductMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.CheckOutHistoryRepository;
import com.sufaltalukder.Repositories.ProductRepository;
import com.sufaltalukder.Repositories.UserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;
import com.sufaltalukder.feign.Services.AddToCartFeignService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CheckOutHistoryMgmtServiceImpl implements CheckOutHistoryMgmtService {

	@Autowired
	private CheckOutHistoryRepository checkOutHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private AddToCartFeignService addToCartFeignService;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<CheckOutDTO> createUserCheckOut(long authUserId, long userId,
			CheckOutHistoryRequest checkOutHistoryRequest) {

		CheckOutHistoryModel addData = new CheckOutHistoryModel();
		double totalPaymentAmount = 0;

		UserModel user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		StringBuilder cartIdsBuilder = new StringBuilder();

		for (String cartIdStr : checkOutHistoryRequest.getAddToCartIds().split(",")) {

			if (cartIdStr.isBlank())
				continue;

			long eachCartId = Long.parseLong(cartIdStr.trim());

			// call Feign
			ApiResponse<ProductAddToCartModel> apiResponse = addToCartFeignService.getUserCart(eachCartId,
					user.getUserId());

			if (apiResponse == null || !"success".equals(apiResponse.getStatus())) {
				return new ApiResponse<>("not found",
						"Cart ID: " + eachCartId + " not found for User ID: " + user.getUserId(), null);
			}

			ProductAddToCartModel cart = apiResponse.getContent();

			totalPaymentAmount += cart.getEachProductTotalPrice();
			cartIdsBuilder.append(eachCartId).append(",");
		}

		// Remove last comma
		String cartIds = cartIdsBuilder.length() > 0 ? cartIdsBuilder.substring(0, cartIdsBuilder.length() - 1) : "";

		addData.setAuthUserInfo(authUser);
		addData.setUserInfo(user);
		addData.setAddToCartIds(cartIds);
		addData.setPaymentAddress(checkOutHistoryRequest.getPaymentAddress());
		addData.setShippingAddress(checkOutHistoryRequest.getShippingAddress());
		addData.setShippingMethod(checkOutHistoryRequest.getShippingMethod());
		addData.setPaymentMethod(checkOutHistoryRequest.getPaymentMethod());
		addData.setPaymentAmount(totalPaymentAmount);
		addData.setDeliveryInDays(checkOutHistoryRequest.getDeliveryInDays());

		if (checkOutHistoryRequest.getPaymentMethod() == PaymentMethod.CCAVENUE
				|| checkOutHistoryRequest.getPaymentMethod() == PaymentMethod.BANK_RTGS_NEFT_TRANSFER) {
			addData.setPaymentStatus("PAYMENT_SUCCESS");
		} else if (checkOutHistoryRequest.getPaymentMethod() == PaymentMethod.COD) {
			addData.setPaymentStatus("PAYMENT_PENDING");
		}

		CheckOutHistoryModel saved = checkOutHistoryRepository.save(addData);

		CheckOutDTO dto = CheckOutHistoryMapper.toDto(saved);

		return new ApiResponse<>("success", "Checkout successfully.", dto);
	}

	@Override
	public ApiResponse<List<CheckOutHistoryDTO>> getAllCheckOutHistories() {

		List<CheckOutHistoryModel> histories = checkOutHistoryRepository.findAllCheckoutHistories();

		if (histories.isEmpty()) {
			return new ApiResponse<>("not found", "Checkout historie(s) not found.", null);
		}

		List<CheckOutHistoryDTO> dtos = histories.stream().map(history -> {

			// Parse productIds String → List<Long>
			List<Long> productIds = List.of();

			if (history.getProductIds() != null && !history.getProductIds().isBlank()) {
				productIds = Arrays.stream(history.getProductIds().split(",")).map(String::trim)
						.filter(id -> !id.isEmpty()).map(Long::valueOf).toList();
			}

			// Fetch product entities
			List<ProductDTO> productDTOs = productIds.isEmpty() ? List.of()
					: productRepository.findByProductIdIn(productIds).stream().map(ProductMapper::toDTO).toList();

			// Map checkout history + products
			return CheckOutHistoryMapper.toDTO(history, productDTOs);

		}).toList();

		return new ApiResponse<>("success", "All checkout historie(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<CheckOutHistoryDTO> getCheckoutDetails(long authUserId, long checkOutHistoryId) {

		Optional<CheckOutHistoryModel> isCheckOutIdExists = checkOutHistoryRepository
				.findByCheckOutHistoryId(checkOutHistoryId);

		if (isCheckOutIdExists.isEmpty()) {
			return new ApiResponse<>("not found", "Checkout ID not found.", null);
		}

		CheckOutHistoryModel checkoutModel = isCheckOutIdExists.get();

		// Parse productIds String → List<Long>
		List<Long> productIds = List.of();

		if (checkoutModel.getProductIds() != null && !checkoutModel.getProductIds().isBlank()) {
			productIds = Arrays.stream(checkoutModel.getProductIds().split(",")).map(String::trim)
					.filter(id -> !id.isEmpty()).map(Long::valueOf).toList();
		}

		// Fetch product entities
		List<ProductDTO> productDTOs = productIds.isEmpty() ? List.of()
				: productRepository.findByProductIdIn(productIds).stream().map(ProductMapper::toDTO).toList();

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Checkout details fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Checkout details fetched successfully.",
				CheckOutHistoryMapper.toDTO(checkoutModel, productDTOs));
	}
}
