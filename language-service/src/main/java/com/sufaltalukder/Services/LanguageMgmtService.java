package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.DTOs.RequestLanguageDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface LanguageMgmtService {

	ApiResponse<LanguageDTO> createLanguage(long authUserId, RequestLanguageDTO requestLanguageDTO);

	ApiResponse<LanguageDTO> getLanguage(long authUserId, long languageId);

	ApiResponse<List<LanguageDTO>> getAllLanguages();

	ApiResponse<LanguageDTO> updateLanguage(long authUserId, long languageId, RequestLanguageDTO requestLanguageDTO);

	ApiResponse<LanguageDTO> deleteLanguage(long authUserId, long languageId);

}
