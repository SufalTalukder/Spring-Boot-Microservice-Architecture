package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.LanguageModel;

public interface LanguageMgmtService {

	ApiResponse<LanguageDTO> createLanguage(LanguageModel languageModel);

	ApiResponse<LanguageDTO> getLanguage(long authUserId, long languageId);

	ApiResponse<List<LanguageDTO>> getAllLanguages();

	ApiResponse<LanguageDTO> updateLanguage(long languageId, LanguageModel languageModel);

	ApiResponse<LanguageDTO> deleteLanguage(long authUserId, long languageId);

}
