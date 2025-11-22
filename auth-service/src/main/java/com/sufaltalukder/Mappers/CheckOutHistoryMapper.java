package com.sufaltalukder.Mappers;

import java.util.Arrays;

import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.CheckOutHistoryModel;

public class CheckOutHistoryMapper {

	public static CheckOutHistoryDTO toDTO(CheckOutHistoryModel model) {
		if (model == null) {
			return null;
		}

		CheckOutHistoryDTO dto = new CheckOutHistoryDTO();
		dto.setCheckOutHistoryId(model.getCheckOutHistoryId());
		dto.setAuthUserId(model.getAuthUserId());
		dto.setUserId(model.getUserId());
		dto.setUserInfo(model.getUserInfo());

		dto.setAddToCartIds(Arrays.stream(model.getAddToCartIds().split(",")).filter(id -> !id.isBlank()).toList());

		dto.setPaymentAddress(model.getPaymentAddress());
		dto.setShippingAddress(model.getShippingAddress());
		dto.setShippingMethod(model.getShippingMethod());
		dto.setPaymentMethod(model.getPaymentMethod());
		dto.setPaymentAmount(model.getPaymentAmount());
		dto.setDeliveryInDays(model.getDeliveryInDays());
		dto.setPaymentStatus(model.getPaymentStatus());
		dto.setOrderStatus(model.getOrderStatus());
		dto.setPaymentDateTime(model.getPaymentDateTime());
		dto.setCheckOutHistoryCreatedAt(model.getCheckOutHistoryCreatedAt());

		return dto;
	}

	public static CheckOutHistoryModel toEntity(CheckOutHistoryDTO dto) {
		if (dto == null) {
			return null;
		}

		CheckOutHistoryModel model = new CheckOutHistoryModel();
		model.setCheckOutHistoryId(dto.getCheckOutHistoryId());
		model.setAuthUserId(dto.getAuthUserId());
		model.setUserId(dto.getUserId());

		model.setAddToCartIds(String.join(",", dto.getAddToCartIds()));

		model.setPaymentAddress(dto.getPaymentAddress());
		model.setShippingAddress(dto.getShippingAddress());
		model.setShippingMethod(dto.getShippingMethod());
		model.setPaymentMethod(dto.getPaymentMethod());
		model.setPaymentAmount(dto.getPaymentAmount());
		model.setDeliveryInDays(dto.getDeliveryInDays());
		model.setPaymentStatus(dto.getPaymentStatus());
		model.setOrderStatus(dto.getOrderStatus());
		model.setPaymentDateTime(dto.getPaymentDateTime());

		return model;
	}
}
