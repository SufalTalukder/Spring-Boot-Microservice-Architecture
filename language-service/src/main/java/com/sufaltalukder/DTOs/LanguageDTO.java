package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.LanguageModel.LanguageActive;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {

	private long languageId;
	private AuthResponseDTO authUserInfo;
	private String languageName;
	private LanguageActive languageActive;
	private Instant languageCreatedAt;
	private Instant languageUpdatedAt;
}
