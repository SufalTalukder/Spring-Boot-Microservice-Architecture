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

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "product_id", nullable = false)
	private long productId;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "productId", insertable = false, updatable = false)
	private ProductModel productInfo;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime favouriteCreatedAt;
}
