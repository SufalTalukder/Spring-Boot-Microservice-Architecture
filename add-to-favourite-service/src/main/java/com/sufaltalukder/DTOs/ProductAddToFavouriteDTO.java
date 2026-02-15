package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAddToFavouriteDTO {

	private long addToFavouriteId;
	private AuthResponseDTO authUserInfo;
	private UserDTO userInfo;
	private ProductDTO productInfo;
	private ZonedDateTime favouriteCreatedAt;

}
