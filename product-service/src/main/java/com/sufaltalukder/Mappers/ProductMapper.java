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

		return new ProductDTO(entity.getProductId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				LanguageMapper.toDTO(entity.getLanguageInfo()), CategoryMapper.toDTO(entity.getCategoryInfo()),
				SubCategoryMapper.toDTO(entity.getSubCategoryInfo()), entity.getProductName(), entity.getProductBrand(),
				entity.getProductCode(), entity.getProductAvailability(), entity.getProductPrice(),
				entity.getProductDetails(), entity.getProductImage(), entity.getProductStock(),
				entity.getProductActive(), entity.getProductCreatedAt(), entity.getProductUpdatedAt());
	}

	public static List<ProductDTO> toDTO(List<ProductModel> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream().map(ProductMapper::toDTO).collect(Collectors.toList());
	}
}
