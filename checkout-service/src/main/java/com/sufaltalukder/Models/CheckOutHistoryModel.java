package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "check_out_history_tbl")
public class CheckOutHistoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long checkOutHistoryId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserModel userInfo;

	@Column(name = "add_to_cart_ids", nullable = false)
	private String addToCartIds;

	@Column(name = "product_ids")
	private String productIds;

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
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant paymentDateTime;

	@CreationTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant checkOutHistoryCreatedAt;
}
