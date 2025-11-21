package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.OrderReturnDTO;
import com.sufaltalukder.Models.OrderReturnModel;

public class OrderReturnMapper {

	public static OrderReturnDTO toDTO(OrderReturnModel model) {
		if (model == null)
			return null;

		OrderReturnDTO dto = new OrderReturnDTO();
		dto.setOrderReturnId(model.getOrderReturnId());
		dto.setCheckOutHistoryId(model.getCheckOutHistoryId());
		dto.setAuthUserId(model.getAuthUserId());
		dto.setUserId(model.getUserId());
		dto.setIsReturn(model.getIsReturn());
		dto.setReturnInDays(model.getReturnInDays());
		dto.setReturnAmount(model.getReturnAmount());
		dto.setReturnAmountStatus(model.getReturnAmountStatus());
		dto.setReturnAmountDateTime(model.getReturnAmountDateTime());
		dto.setOrderReturnCreatedAt(model.getOrderReturnCreatedAt());

		return dto;
	}

	public static OrderReturnModel toEntity(OrderReturnDTO dto) {
		if (dto == null)
			return null;

		OrderReturnModel model = new OrderReturnModel();
		model.setOrderReturnId(dto.getOrderReturnId());
		model.setCheckOutHistoryId(dto.getCheckOutHistoryId());
		model.setAuthUserId(dto.getAuthUserId());
		model.setUserId(dto.getUserId());
		model.setIsReturn(dto.getIsReturn());
		model.setReturnInDays(dto.getReturnInDays());
		model.setReturnAmount(dto.getReturnAmount());
		model.setReturnAmountStatus(dto.getReturnAmountStatus());
		model.setReturnAmountDateTime(dto.getReturnAmountDateTime());
		model.setOrderReturnCreatedAt(dto.getOrderReturnCreatedAt());

		return model;
	}
}
