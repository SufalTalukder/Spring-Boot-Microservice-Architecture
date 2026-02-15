package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.LanguageModel.LanguageActive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageResponseDTO {

	private long languageId;
	private String languageName;
	private LanguageActive languageActive;

}
