package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_tbl")
public class ProductModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "language_id")
	private LanguageModel languageInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private CategoryModel categoryInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_category_id")
	private SubCategoryModel subCategoryInfo;

	@Column(name = "product_name", nullable = false)
	private String productName;

	@Column(name = "product_brand")
	private String productBrand;

	@Column(name = "product_code", nullable = false)
	private long productCode;

	@Column(name = "product_availability", nullable = false)
	private int productAvailability;

	@Column(name = "product_price", nullable = false)
	private double productPrice;

	@Lob
	@Column(name = "product_details", columnDefinition = "LONGTEXT")
	private String productDetails;

	@Column(name = "product_image")
	private String productImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "product_stock", nullable = false)
	private ProductStock productStock;

	public enum ProductStock {
		IN_STOCK, OUT_OF_STOCK
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private ProductActive productActive;

	public enum ProductActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime productCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime productUpdatedAt;

}
