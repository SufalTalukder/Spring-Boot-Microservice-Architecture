package com.sufaltalukder.DTOs;

import java.time.Instant;
import java.util.List;

import com.sufaltalukder.Models.CheckOutHistoryModel.OrderStatus;
import com.sufaltalukder.Models.CheckOutHistoryModel.PaymentMethod;
import com.sufaltalukder.Models.CheckOutHistoryModel.ShippingMethod;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutHistoryDTO {

	private long checkOutHistoryId;
	private AuthUserDTO authUserInfo;
	private UserDTO userInfo;
	private List<String> addToCartIds;
	private List<String> productIds;
	private List<ProductDTO> products;
	private String paymentAddress;
	private String shippingAddress;	
	private ShippingMethod shippingMethod;
	private PaymentMethod paymentMethod;
	private double paymentAmount;
	private String deliveryInDays;
	private String paymentStatus;
	private OrderStatus orderStatus;
	private Instant paymentDateTime;
	private Instant checkOutHistoryCreatedAt;
}
