package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddToCartDTO {

	private long addToCartId;
	private long authUserId;
	private long userId;
	private long productId;
	private int quantity;
	private double eachProductTotalPrice;
	private ZonedDateTime cartCreatedAt;
	private ZonedDateTime cartUpdatedAt;
}
