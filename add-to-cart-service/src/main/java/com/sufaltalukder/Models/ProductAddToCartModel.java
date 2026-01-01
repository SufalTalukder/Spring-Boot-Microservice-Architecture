package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "add_to_cart_tbl")
public class ProductAddToCartModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long addToCartId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserModel userInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductModel productInfo;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "each_product_total_price")
	private double eachProductTotalPrice;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime cartCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime cartUpdatedAt;
}
