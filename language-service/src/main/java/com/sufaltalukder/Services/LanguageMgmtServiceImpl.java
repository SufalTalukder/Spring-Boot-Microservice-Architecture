package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.LanguageDTO;
import com.sufaltalukder.Mappers.LanguageMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.LanguageModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.LanguageRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class LanguageMgmtServiceImpl implements LanguageMgmtService {

	@Autowired
	private LanguageRepository languageRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<LanguageDTO> createLanguage(LanguageModel model) {
		LanguageModel exists = languageRepository.findByLanguageName(model.getLanguageName());

		if (exists != null) {
			return new ApiResponse<>("exist", "Language already exists!", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(model.getAuthUserId());
		actionLogData.setAuthUserId(model.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Language created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		LanguageModel saved = languageRepository.save(model);
		return new ApiResponse<>("success", "Language created successfully.", LanguageMapper.toDTO(saved));
	}

	@Override
	public ApiResponse<LanguageDTO> getLanguage(long authUserId, long languageId) {
		Optional<LanguageModel> entityOpt = languageRepository.findById(languageId);

		if (entityOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Language fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

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

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(model.getAuthUserId());
		actionLogData.setAuthUserId(model.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Language updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		LanguageModel updated = languageRepository.save(entity);
		return new ApiResponse<>("success", "Language updated successfully.", LanguageMapper.toDTO(updated));
	}

	@Override
	public ApiResponse<LanguageDTO> deleteLanguage(long authUserId, long languageId) {
		Optional<LanguageModel> entityOpt = languageRepository.findById(languageId);

		if (entityOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Language not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Language deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		languageRepository.deleteById(languageId);
		return new ApiResponse<>("success", "Language deleted successfully.", null);
	}

}
