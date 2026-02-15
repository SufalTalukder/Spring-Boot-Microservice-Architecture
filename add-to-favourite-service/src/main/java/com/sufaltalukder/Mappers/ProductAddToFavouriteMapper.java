package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;

public class ProductAddToFavouriteMapper {

	public static ProductAddToFavouriteDTO toDTO(ProductAddToFavouriteModel entity) {
		if (entity == null) {
			return null;
		}

		return new ProductAddToFavouriteDTO(entity.getAddToFavouriteId(),
				AuthUserMapper.toDTO(entity.getAuthUserInfo()), UserMapper.toDTO(entity.getUserInfo()),
				ProductMapper.toDTO(entity.getProductInfo()), entity.getFavouriteCreatedAt());
	}
}
