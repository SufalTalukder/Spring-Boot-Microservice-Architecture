package com.sufaltalukder.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.sufaltalukder.DTOs.ProductAddToCartDTO;
import com.sufaltalukder.Models.ProductAddToCartModel;

public class ProductAddToCartMapper {

	public static ProductAddToCartDTO toDTO(ProductAddToCartModel model) {
		if (model == null) {
			return null;
		}

		ProductAddToCartDTO dto = new ProductAddToCartDTO();
		dto.setAddToCartId(model.getAddToCartId());
		dto.setAuthUserId(model.getAuthUserId());
		dto.setUserId(model.getUserId());
		dto.setProductId(model.getProductId());
		dto.setQuantity(model.getQuantity());
		dto.setEachProductTotalPrice(model.getEachProductTotalPrice());
		dto.setCartCreatedAt(model.getCartCreatedAt());
		dto.setCartUpdatedAt(model.getCartUpdatedAt());

		return dto;
	}

	public static List<ProductAddToCartDTO> toDTO(List<ProductAddToCartModel> models) {
		if (models == null) {
			return null;
		}

		return models.stream().map(ProductAddToCartMapper::toDTO).collect(Collectors.toList());
	}

	public static ProductAddToCartModel toEntity(ProductAddToCartDTO dto) {
		if (dto == null) {
			return null;
		}

		ProductAddToCartModel model = new ProductAddToCartModel();
		model.setAddToCartId(dto.getAddToCartId());
		model.setAuthUserId(dto.getAuthUserId());
		model.setUserId(dto.getUserId());
		model.setProductId(dto.getProductId());
		model.setQuantity(dto.getQuantity());
		model.setEachProductTotalPrice(dto.getEachProductTotalPrice());
		model.setCartCreatedAt(dto.getCartCreatedAt());
		model.setCartUpdatedAt(dto.getCartUpdatedAt());

		return model;
	}
}
