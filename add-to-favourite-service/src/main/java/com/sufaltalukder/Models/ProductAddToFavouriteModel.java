package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "add_to_favourite_tbl")
public class ProductAddToFavouriteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long addToFavouriteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private UserModel userInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private ProductModel productInfo;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime favouriteCreatedAt;
}
