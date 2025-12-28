package com.sufaltalukder.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.sufaltalukder.DTOs.ProductDTO;
import com.sufaltalukder.Models.ProductModel;

public class ProductMapper {

	public static ProductDTO toDTO(ProductModel entity) {
		if (entity == null) {
			return null;
		}

		return new ProductDTO(entity.getProductId(), entity.getAuthUserId(), entity.getAuthUserInfo(),
				entity.getLanguageId(), entity.getLanguageInfo(), entity.getCategoryId(), entity.getCategorInfo(),
				entity.getSubCategoryId(), entity.getSubCategoryInfo(), entity.getProductName(),
				entity.getProductBrand(), entity.getProductCode(), entity.getProductAvailability(),
				entity.getProductPrice(), entity.getProductDetails(), entity.getProductImage(),
				entity.getProductStock(), entity.getProductActive(), entity.getProductCreatedAt(),
				entity.getProductUpdatedAt());
	}

	public static ProductModel toEntity(ProductDTO dto) {
		if (dto == null) {
			return null;
		}

		ProductModel entity = new ProductModel();

		entity.setProductId(dto.getProductId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setAuthUserInfo(dto.getAuthUserInfo());
		entity.setLanguageId(dto.getLanguageId());
		entity.setLanguageInfo(dto.getLanguageInfo());
		entity.setCategoryId(dto.getCategoryId());
		entity.setCategorInfo(dto.getCategorInfo());
		entity.setSubCategoryId(dto.getSubCategoryId());
		entity.setSubCategoryInfo(dto.getSubCategoryInfo());
		entity.setProductName(dto.getProductName());
		entity.setProductBrand(dto.getProductBrand());
		entity.setProductCode(dto.getProductCode());
		entity.setProductAvailability(dto.getProductAvailability());
		entity.setProductPrice(dto.getProductPrice());
		entity.setProductDetails(dto.getProductDetails());
		entity.setProductImage(dto.getProductImage());
		entity.setProductStock(dto.getProductStock());
		entity.setProductActive(dto.getProductActive());
		entity.setProductCreatedAt(dto.getProductCreatedAt());
		entity.setProductUpdatedAt(dto.getProductUpdatedAt());

		return entity;
	}

	public static List<ProductDTO> toDTO(List<ProductModel> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
	}
}
