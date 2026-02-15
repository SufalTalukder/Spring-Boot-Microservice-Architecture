package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.CategoryResponseDTO;
import com.sufaltalukder.Models.CategoryModel;

public class CategoryMapper {

	public static CategoryResponseDTO toDTO(CategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new CategoryResponseDTO(entity.getCategoryId(), entity.getCategoryName(), entity.getCategoryActive());
	}
}
