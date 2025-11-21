package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.ProductModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddToFavouriteDTO {

	private long addToFavouriteId;
	private long authUserId;
	private long userId;
	private long productId;
	private ProductModel productInfo;
	private ZonedDateTime favouriteCreatedAt;
}
