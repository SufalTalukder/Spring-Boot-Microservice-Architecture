package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.SubCategoryResponseDTO;
import com.sufaltalukder.Models.SubCategoryModel;

public class SubCategoryMapper {

	public static SubCategoryResponseDTO toDTO(SubCategoryModel entity) {
		if (entity == null) {
			return null;
		}

		return new SubCategoryResponseDTO(entity.getSubCategoryId(), entity.getSubCategoryName(),
				entity.getSubCategoryActive());
	}
}
