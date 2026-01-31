package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.ProductModel.ProductActive;
import com.sufaltalukder.Models.ProductModel.ProductStock;

import lombok.Data;

@Data
public class RequestProductDTO {

	private String productName;
	private String productBrand;
	private long productCode;
	private int productAvailability;
	private double productPrice;
	private String productDetails;
	private ProductStock productStock;
	private ProductActive productActive;

}
