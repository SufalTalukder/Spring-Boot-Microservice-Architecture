package com.sufaltalukder.Mappers;

import java.util.Arrays;
import java.util.List;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Models.CheckOutHistoryModel;

public class CheckOutHistoryMapper {

	public static CheckOutHistoryDTO toDTO(CheckOutHistoryModel m, List<ProductDTO> products) {

		List<String> cartIds = (m.getAddToCartIds() != null && !m.getAddToCartIds().isBlank())
				? Arrays.stream(m.getAddToCartIds().split(",")).map(String::trim).toList()
				: List.of();

		List<String> productIds = (m.getProductIds() != null && !m.getProductIds().isBlank())
				? Arrays.stream(m.getProductIds().split(",")).map(String::trim).toList()
				: List.of();

		CheckOutHistoryDTO dto = new CheckOutHistoryDTO();

		dto.setCheckOutHistoryId(m.getCheckOutHistoryId());
		dto.setAuthUserInfo(AuthUserMapper.toDTO(m.getAuthUserInfo()));
		dto.setUserInfo(UserMapper.toDTO(m.getUserInfo()));
		dto.setAddToCartIds(cartIds);
		dto.setProductIds(productIds);
		dto.setProducts(products);
		dto.setPaymentAddress(m.getPaymentAddress());
		dto.setShippingAddress(m.getShippingAddress());
		dto.setShippingMethod(m.getShippingMethod());
		dto.setPaymentMethod(m.getPaymentMethod());
		dto.setPaymentAmount(m.getPaymentAmount());
		dto.setDeliveryInDays(m.getDeliveryInDays());
		dto.setPaymentStatus(m.getPaymentStatus());
		dto.setOrderStatus(m.getOrderStatus());
		dto.setPaymentDateTime(m.getPaymentDateTime());
		dto.setCheckOutHistoryCreatedAt(m.getCheckOutHistoryCreatedAt());

		return dto;
	}

	public static CheckOutDTO toDto(CheckOutHistoryModel m) {

		List<String> cartIds = Arrays.asList(m.getAddToCartIds().split(","));

		List<String> productIds = Arrays.asList(m.getProductIds().split(","));

		return new CheckOutDTO(m.getCheckOutHistoryId(), m.getAuthUserInfo(), m.getUserInfo(), cartIds, productIds,
				m.getPaymentAddress(), m.getShippingAddress(), m.getShippingMethod(), m.getPaymentMethod(),
				m.getPaymentAmount(), m.getDeliveryInDays(), m.getPaymentStatus(), m.getOrderStatus(),
				m.getPaymentDateTime(), m.getCheckOutHistoryCreatedAt());
	}
}
