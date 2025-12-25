package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.SubCategoryModel;

public class SubCategoryMapper {

	public static SubCategoryDTO toDTO(SubCategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new SubCategoryDTO(entity.getSubCategoryId(), entity.getAuthUserInfo(), entity.getSubCategoryName(),
				entity.getSubCategoryImage(), entity.getSubCategoryActive(), entity.getSubCategoryCreatedAt(),
				entity.getSubCategoryUpdatedAt());
	}

	public static SubCategoryModel toEntity(SubCategoryDTO dto) {
		if (dto == null) {
			return null;
		}

		SubCategoryModel entity = new SubCategoryModel();

		entity.setSubCategoryId(dto.getSubCategoryId());
		entity.setAuthUserInfo(dto.getAuthUserInfo());
		entity.setSubCategoryName(dto.getSubCategoryName());
		entity.setSubCategoryImage(dto.getSubCategoryImage());
		entity.setSubCategoryActive(dto.getSubCategoryActive());
		entity.setSubCategoryCreatedAt(dto.getSubCategoryCreatedAt());
		entity.setSubCategoryUpdatedAt(dto.getSubCategoryUpdatedAt());

		return entity;
	}
}
