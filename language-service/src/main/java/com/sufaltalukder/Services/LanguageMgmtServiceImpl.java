package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Mappers.LanguageMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Repositories.LanguageRepository;

@Service
public class LanguageMgmtServiceImpl implements LanguageMgmtService {

	@Autowired
	private LanguageRepository languageRepository;

	@Override
	public ApiResponse<LanguageDTO> createLanguage(LanguageModel model) {
		LanguageModel exists = languageRepository.findByLanguageName(model.getLanguageName());

		if (exists != null) {
			return new ApiResponse<>("exist", "Language already exists!", null);
		}

		LanguageModel saved = languageRepository.save(model);
		return new ApiResponse<>("success", "Language created successfully.", LanguageMapper.toDTO(saved));
	}

	@Override
	public ApiResponse<LanguageDTO> getLanguage(long languageId) {
		Optional<LanguageModel> entityOpt = languageRepository.findById(languageId);

		if (entityOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		LanguageDTO dto = LanguageMapper.toDTO(entityOpt.get());
		return new ApiResponse<>("success", "Language fetched successfully.", dto);
	}

	@Override
	public ApiResponse<List<LanguageDTO>> getAllLanguages() {
		List<LanguageModel> entities = languageRepository.findAll();

		if (entities.isEmpty()) {
			return new ApiResponse<>("not found", "No languages found.", null);
		}

		List<LanguageDTO> dtos = entities.stream().map(LanguageMapper::toDTO).toList();

		return new ApiResponse<>("success", "Languages fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<LanguageDTO> updateLanguage(long languageId, LanguageModel model) {
		Optional<LanguageModel> entityOpt = languageRepository.findById(languageId);

		if (entityOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		LanguageModel exists = languageRepository.findByLanguageName(model.getLanguageName());

		if (exists != null && exists.getLanguageId() != languageId) {
			return new ApiResponse<>("exist", "Language name already used!", null);
		}

		LanguageModel entity = entityOpt.get();
		entity.setLanguageName(model.getLanguageName());
		entity.setLanguageActive(model.getLanguageActive());
		entity.setLanguageUpdatedAt(ZonedDateTime.now());

		LanguageModel updated = languageRepository.save(entity);
		return new ApiResponse<>("success", "Language updated successfully.", LanguageMapper.toDTO(updated));
	}

	@Override
	public ApiResponse<LanguageDTO> deleteLanguage(long languageId) {
		Optional<LanguageModel> entityOpt = languageRepository.findById(languageId);

		if (entityOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		languageRepository.deleteById(languageId);
		return new ApiResponse<>("success", "Language deleted successfully.", null);
	}

}
