package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.LanguageModel.LanguageActive;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {

	private long languageId;
	private long authUserId;
	private String languageName;
	private LanguageActive languageActive;
	private ZonedDateTime languageCreatedAt;
	private ZonedDateTime languageUpdatedAt;
}
