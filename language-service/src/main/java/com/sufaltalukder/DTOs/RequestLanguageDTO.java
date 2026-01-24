package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.LanguageModel.LanguageActive;

import lombok.Data;

@Data
public class RequestLanguageDTO {

	private String languageName;
	private LanguageActive languageActive;

}
