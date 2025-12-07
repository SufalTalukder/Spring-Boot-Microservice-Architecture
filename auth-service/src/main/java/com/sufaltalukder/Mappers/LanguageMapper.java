package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Models.LanguageModel;

public class LanguageMapper {

	public static LanguageDTO toDTO(LanguageModel entity) {
		return new LanguageDTO(entity.getLanguageId(), entity.getAuthUserId(), entity.getLanguageName(),
				entity.getLanguageActive(), entity.getLanguageCreatedAt(), entity.getLanguageUpdatedAt());
	}

	public static LanguageModel toEntity(LanguageDTO dto) {
		LanguageModel entity = new LanguageModel();
		entity.setLanguageId(dto.getLanguageId());
		entity.setLanguageName(dto.getLanguageName());
		entity.setLanguageActive(dto.getLanguageActive());
		return entity;
	}
}
