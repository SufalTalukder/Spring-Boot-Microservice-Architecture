package com.sufaltalukder.Mappers;

import java.util.*;

import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.CheckOutHistoryModel;

public class CheckOutHistoryMapper {

	public static CheckOutHistoryDTO toDTO(CheckOutHistoryModel m) {

		List<String> ids = Arrays.asList(m.getAddToCartIds().split(","));

		return new CheckOutHistoryDTO(m.getCheckOutHistoryId(), m.getAuthUserId(), m.getUserId(), ids, m.getUserInfo(),
				m.getPaymentAddress(), m.getShippingAddress(), m.getShippingMethod(), m.getPaymentMethod(),
				m.getPaymentAmount(), m.getDeliveryInDays(), m.getPaymentStatus(), m.getOrderStatus(),
				m.getPaymentDateTime(), m.getCheckOutHistoryCreatedAt());
	}
}
