package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.ProductAddToFavouriteDTO;
import com.sufaltalukder.Models.ProductAddToFavouriteModel;

public class ProductAddToFavouriteMapper {

	public static ProductAddToFavouriteDTO toDTO(ProductAddToFavouriteModel entity) {
		if (entity == null) {
			return null;
		}

		return new ProductAddToFavouriteDTO(entity.getAddToFavouriteId(), entity.getAuthUserId(), entity.getUserId(),
				entity.getProductId(), entity.getProductInfo(), entity.getFavouriteCreatedAt());
	}

	public static ProductAddToFavouriteModel toEntity(ProductAddToFavouriteDTO dto) {
		if (dto == null) {
			return null;
		}

		ProductAddToFavouriteModel entity = new ProductAddToFavouriteModel();

		entity.setAddToFavouriteId(dto.getAddToFavouriteId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setUserId(dto.getUserId());
		entity.setProductId(dto.getProductId());
		entity.setProductInfo(dto.getProductInfo());
		entity.setFavouriteCreatedAt(dto.getFavouriteCreatedAt());

		return entity;
	}
}
