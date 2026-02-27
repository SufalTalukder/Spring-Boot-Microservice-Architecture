package com.sufaltalukder.DTOs;

import java.util.List;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.CheckOutHistoryModel.OrderStatus;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Models.CheckOutHistoryModel.ShippingMethod;

import lombok.*;

@Data
public class CheckOutHistoryRequest {

	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private String addToCartIds;
	private String productIds;
	private List<ProductDTO> products;
	private String paymentAddress;
	private String shippingAddress;
	private ShippingMethod shippingMethod;
	private PaymentMethod paymentMethod;
	private double paymentAmount;
	private String deliveryInDays;
	private String paymentStatus;
	private OrderStatus orderStatus;
}
