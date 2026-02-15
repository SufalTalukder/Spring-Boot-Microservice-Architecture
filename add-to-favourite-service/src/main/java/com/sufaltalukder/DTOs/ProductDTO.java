package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.ProductModel.ProductActive;
import com.sufaltalukder.Models.ProductModel.ProductStock;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	private long productId;
	private AuthResponseDTO authUserInfo;
	private LanguageResponseDTO languageInfo;
	private CategoryResponseDTO categoryInfo;
	private SubCategoryResponseDTO subCategoryInfo;
	private String productName;
	private String productBrand;
	private long productCode;
	private int productAvailability;
	private double productPrice;
	private String productDetails;
	private String productImage;
	private ProductStock productStock;
	private ProductActive productActive;
	private Instant productCreatedAt;
	private Instant productUpdatedAt;

}
