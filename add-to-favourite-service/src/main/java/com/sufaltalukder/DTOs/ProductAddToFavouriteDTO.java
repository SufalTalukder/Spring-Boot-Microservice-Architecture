package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Models.UserModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddToFavouriteDTO {

	private long addToFavouriteId;
	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private ProductModel productInfo;
	private ZonedDateTime favouriteCreatedAt;
}
