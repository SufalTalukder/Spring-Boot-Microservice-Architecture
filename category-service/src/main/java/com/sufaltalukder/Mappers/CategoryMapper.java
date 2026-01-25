package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.Models.CategoryModel;

public class CategoryMapper {

	public static CategoryDTO toDTO(CategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new CategoryDTO(entity.getCategoryId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				entity.getCategoryName(), entity.getCategoryImage(), entity.getCategoryActive(),
				entity.getCategoryCreatedAt(), entity.getCategoryUpdatedAt());
	}
}
