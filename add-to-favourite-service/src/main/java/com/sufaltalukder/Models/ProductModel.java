package com.sufaltalukder.Models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product_tbl", indexes = { @Index(name = "idx_product_code", columnList = "product_code", unique = true),
		@Index(name = "idx_product_name", columnList = "product_name"),
		@Index(name = "idx_product_brand", columnList = "product_brand"),
		@Index(name = "idx_product_stock", columnList = "product_stock"),
		@Index(name = "idx_product_status", columnList = "status"),
		@Index(name = "idx_product_created_at", columnList = "created_at"),

		// Foreign key indexes
		@Index(name = "idx_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_language_id", columnList = "language_id"),
		@Index(name = "idx_category_id", columnList = "category_id"),
		@Index(name = "idx_sub_category_id", columnList = "sub_category_id") })
public class ProductModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long productId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "language_id")
	private LanguageModel languageInfo;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryModel categoryInfo;

	@ManyToOne
	@JoinColumn(name = "sub_category_id")
	private SubCategoryModel subCategoryInfo;

	@Column(name = "product_name", nullable = false)
	private String productName;

	@Column(name = "product_brand")
	private String productBrand;

	@Column(name = "product_code", nullable = false, unique = true)
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private java.time.Instant productCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private java.time.Instant productUpdatedAt;
}
