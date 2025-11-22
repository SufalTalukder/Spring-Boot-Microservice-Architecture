package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "check_out_history_tbl")
public class CheckOutHistoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long checkOutHistoryId;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserModel userInfo;

	@Column(name = "add_to_cart_ids", nullable = false)
	private String addToCartIds;

	@Column(name = "payment_address", nullable = false)
	private String paymentAddress;

	@Column(name = "shipping_address", nullable = false)
	private String shippingAddress;

	@Enumerated(EnumType.STRING)
	@Column(name = "shipping_method", nullable = false)
	private ShippingMethod shippingMethod;

	public enum ShippingMethod {
		COURIER_INDIA_POST_SHIPPING
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;

	public enum PaymentMethod {
		CCAVENUE, BANK_RTGS_NEFT_TRANSFER, COD
	}

	@Column(name = "payment_amount")
	private double paymentAmount;

	@Column(name = "delivery_in_days")
	private String deliveryInDays;

	@Column(name = "payment_status")
	private String paymentStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	public enum OrderStatus {
		SUCCESSFUL, CANCELLED
	}

	@UpdateTimestamp
	@Column(name = "payment_datetime")
	private ZonedDateTime paymentDateTime;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime checkOutHistoryCreatedAt;
}
