package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "add_to_favourite_tbl", indexes = { @Index(name = "idx_fav_auth_user", columnList = "auth_user_id"),
		@Index(name = "idx_fav_user", columnList = "user_id"),
		@Index(name = "idx_fav_product", columnList = "product_id"),
		@Index(name = "idx_fav_created_at", columnList = "created_at"),
		@Index(name = "uk_user_product_favourite", columnList = "user_id, product_id", unique = true) })
public class ProductAddToFavouriteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long addToFavouriteId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserModel userInfo;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private ProductModel productInfo;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private ZonedDateTime favouriteCreatedAt;
}
