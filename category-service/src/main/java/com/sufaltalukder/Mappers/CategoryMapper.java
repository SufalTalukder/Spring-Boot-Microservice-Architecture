package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.Models.CategoryModel;

public class CategoryMapper {

	public static CategoryDTO toDTO(CategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new CategoryDTO(entity.getCategoryId(), entity.getAuthUserInfo(), entity.getCategoryName(),
				entity.getCategoryImage(), entity.getCategoryActive(), entity.getCategoryCreatedAt(),
				entity.getCategoryUpdatedAt());
	}

	public static CategoryModel toEntity(CategoryDTO dto) {
		if (dto == null) {
			return null;
		}

		CategoryModel entity = new CategoryModel();

		entity.setCategoryId(dto.getCategoryId());
		entity.setAuthUserInfo(dto.getAuthUserInfo());
		entity.setCategoryName(dto.getCategoryName());
		entity.setCategoryImage(dto.getCategoryImage());
		entity.setCategoryActive(dto.getCategoryActive());
		entity.setCategoryCreatedAt(dto.getCategoryCreatedAt());
		entity.setCategoryUpdatedAt(dto.getCategoryUpdatedAt());

		return entity;
	}
}
