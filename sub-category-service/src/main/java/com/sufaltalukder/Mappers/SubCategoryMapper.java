package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.SubCategoryModel;

public class SubCategoryMapper {

	public static SubCategoryDTO toDTO(SubCategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new SubCategoryDTO(entity.getSubCategoryId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				entity.getSubCategoryName(), entity.getSubCategoryImage(), entity.getSubCategoryActive(),
				entity.getSubCategoryCreatedAt(), entity.getSubCategoryUpdatedAt());
	}
}
