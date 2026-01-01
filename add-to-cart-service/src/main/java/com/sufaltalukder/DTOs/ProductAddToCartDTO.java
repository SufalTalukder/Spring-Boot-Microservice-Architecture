package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddToCartDTO {

	private long addToCartId;
	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private ProductModel productInfo;
	private int quantity;
	private double eachProductTotalPrice;
	private ZonedDateTime cartCreatedAt;
	private ZonedDateTime cartUpdatedAt;
}
