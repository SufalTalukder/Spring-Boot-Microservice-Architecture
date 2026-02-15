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

	@Column(name = "auth_user_Id", nullable = false)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", referencedColumnName = "authUserId", insertable = false, updatable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "language_id", nullable = false)
	private long languageId;

	@ManyToOne
	@JoinColumn(name = "language_id", referencedColumnName = "languageId", insertable = false, updatable = false)
	private LanguageModel languageInfo;

	@Column(name = "category_id", nullable = false)
	private long categoryId;

	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "categoryId", insertable = false, updatable = false)
	private CategoryModel categorInfo;

	@Column(name = "sub_category_id", nullable = false)
	private long subCategoryId;

	@ManyToOne
	@JoinColumn(name = "sub_category_id", referencedColumnName = "subCategoryId", insertable = false, updatable = false)
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
