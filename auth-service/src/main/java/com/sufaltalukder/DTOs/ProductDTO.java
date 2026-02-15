package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ProductModel.ProductActive;
import com.sufaltalukder.Models.ProductModel.ProductStock;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	private long productId;
	private long authUserId;
	private AuthUserModel authUserInfo;
	private long languageId;
	private LanguageModel languageInfo;
	private long categoryId;
	private CategoryModel categorInfo;
	private long subCategoryId;
	private SubCategoryModel subCategoryInfo;
	private String productName;
	private String productBrand;
	private long productCode;
	private int productAvailability;
	private double productPrice;
	private String productDetails;
	private String productImage;
	private ProductStock productStock;
	private ProductActive productActive;
	private ZonedDateTime productCreatedAt;
	private ZonedDateTime productUpdatedAt;
}
