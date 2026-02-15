package com.sufaltalukder.Services;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Mappers.CheckOutHistoryMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Models.ProductAddToCartModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.CheckOutHistoryRepository;
import com.sufaltalukder.Repositories.ProductAddToCartRepository;
import com.sufaltalukder.Repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CheckOutHistoryMgmtServiceImpl implements CheckOutHistoryMgmtService {

	@Autowired
	private CheckOutHistoryRepository checkOutHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductAddToCartRepository productAddToCartRepository;

	@Override
	public ApiResponse<CheckOutHistoryDTO> createUserCheckOut(CheckOutHistoryModel checkOutHistoryModel) {
		CheckOutHistoryModel addData = new CheckOutHistoryModel();
		double totalPaymentAmount = 0;

		UserModel user = userRepository.findById(checkOutHistoryModel.getUserId()).orElse(null);

		if (user == null) {
			return new ApiResponse<>("not found", "User ID not found.", null);
		}

		StringBuilder cartIdsBuilder = new StringBuilder();

		for (String cartIdStr : checkOutHistoryModel.getAddToCartIds().split(",")) {
			long eachCartId = Long.parseLong(cartIdStr.trim());

			ProductAddToCartModel cart = productAddToCartRepository.findCartByUserId(eachCartId, user.getUserId());

			if (cart == null) {
				return new ApiResponse<>("not found",
						"Cart ID: " + eachCartId + " not found for User ID: " + user.getUserId(), null);
			}

			cartIdsBuilder.append(eachCartId).append(",");
			totalPaymentAmount += cart.getEachProductTotalPrice();
		}

		// Remove last comma
		String cartIds = cartIdsBuilder.length() > 0 ? cartIdsBuilder.substring(0, cartIdsBuilder.length() - 1) : "";

		addData.setAuthUserId(checkOutHistoryModel.getAuthUserId());
		addData.setUserId(user.getUserId());
		addData.setAddToCartIds(cartIds);
		addData.setPaymentAddress(checkOutHistoryModel.getPaymentAddress());
		addData.setShippingAddress(checkOutHistoryModel.getShippingAddress());
		addData.setShippingMethod(checkOutHistoryModel.getShippingMethod());
		addData.setPaymentMethod(checkOutHistoryModel.getPaymentMethod());
		addData.setPaymentAmount(totalPaymentAmount);
		addData.setDeliveryInDays(checkOutHistoryModel.getDeliveryInDays());
		addData.setPaymentDateTime(ZonedDateTime.now());

		if (checkOutHistoryModel.getPaymentMethod() == PaymentMethod.CCAVENUE
				|| checkOutHistoryModel.getPaymentMethod() == PaymentMethod.BANK_RTGS_NEFT_TRANSFER) {
			addData.setPaymentStatus("PAYMENT_SUCCESS");
		} else if (checkOutHistoryModel.getPaymentMethod() == PaymentMethod.COD) {
			addData.setPaymentStatus("PAYMENT_PENDING");
		}

		CheckOutHistoryModel saved = checkOutHistoryRepository.save(addData);

		CheckOutHistoryDTO dto = CheckOutHistoryMapper.toDTO(saved);

		return new ApiResponse<>("success", "Checkout successfully.", dto);
	}
}
