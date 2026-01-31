package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.LanguageResponseDTO;
import com.sufaltalukder.Models.LanguageModel;

public class LanguageMapper {

	public static LanguageResponseDTO toDTO(LanguageModel entity) {
		if (entity == null)
			return null;

		return new LanguageResponseDTO(entity.getLanguageId(), entity.getLanguageName(), entity.getLanguageActive());
	}
}
