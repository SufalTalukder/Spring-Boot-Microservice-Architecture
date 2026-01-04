package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;
import java.util.List;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.CheckOutHistoryModel.OrderStatus;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Models.CheckOutHistoryModel.ShippingMethod;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutDTO {

	private long checkOutHistoryId;
	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private List<String> addToCartIds;
	private List<String> productIds;
	private String paymentAddress;
	private String shippingAddress;
	private ShippingMethod shippingMethod;
	private PaymentMethod paymentMethod;
	private double paymentAmount;
	private String deliveryInDays;
	private String paymentStatus;
	private OrderStatus orderStatus;
	private ZonedDateTime paymentDateTime;
	private ZonedDateTime checkOutHistoryCreatedAt;
}
