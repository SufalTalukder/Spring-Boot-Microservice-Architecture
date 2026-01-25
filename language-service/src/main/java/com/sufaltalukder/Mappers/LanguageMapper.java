package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Models.LanguageModel;

public class LanguageMapper {

	public static LanguageDTO toDTO(LanguageModel entity) {
		if (entity == null)
			return null;

		return new LanguageDTO(entity.getLanguageId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				entity.getLanguageName(), entity.getLanguageActive(), entity.getLanguageCreatedAt(),
				entity.getLanguageUpdatedAt());
	}
}
