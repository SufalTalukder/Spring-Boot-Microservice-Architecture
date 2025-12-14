package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Mappers.SubCategoryMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.SubCategoryRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class SubCategoryMgmtServiceImpl implements SubCategoryMgmtService {

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<SubCategoryDTO> createSubCategory(SubCategoryModel subCategoryModel) {
		SubCategoryModel isSubCategoryNameExist = subCategoryRepository
				.findBySubCategoryName(subCategoryModel.getSubCategoryName());

		if (isSubCategoryNameExist != null) {
			return new ApiResponse<>("exist", "SubCategory already exist!", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(subCategoryModel.getAuthUserId());
		actionLogData.setAuthUserId(subCategoryModel.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("SubCategory added successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		SubCategoryModel saveData = subCategoryRepository.save(subCategoryModel);
		return new ApiResponse<>("success", "SubCategory added successfully.", SubCategoryMapper.toDTO(saveData));
	}

	@Override
	public ApiResponse<List<SubCategoryDTO>> getAllSubCategories() {
		List<SubCategoryModel> fetchedAllData = subCategoryRepository.findAll();

		if (fetchedAllData.isEmpty()) {
			return new ApiResponse<>("not found", "No subcategori(s) found.", null);
		}

		List<SubCategoryDTO> dtos = fetchedAllData.stream().map(SubCategoryMapper::toDTO).toList();

		return new ApiResponse<>("success", "All subcategorie(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<SubCategoryDTO> getSubCategory(long authUserId, long subCategoryId) {
		Optional<SubCategoryModel> isCategoryIdExist = subCategoryRepository.findById(subCategoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("SubCategory fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "SubCategory fetched successfully.",
				SubCategoryMapper.toDTO(isCategoryIdExist.get()));
	}

	@Override
	public ApiResponse<SubCategoryDTO> updateSubCategory(long subCategoryId, SubCategoryModel subCategoryModel) {
		Optional<SubCategoryModel> isSubCategoryIdExist = subCategoryRepository.findById(subCategoryId);

		if (isSubCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		SubCategoryModel isSubCategoryNameExist = subCategoryRepository
				.findBySubCategoryName(subCategoryModel.getSubCategoryName());

		if (isSubCategoryNameExist != null && isSubCategoryNameExist.getSubCategoryId() != subCategoryId) {
			return new ApiResponse<>("exist", "SubCategory name already used!", null);
		}

		SubCategoryModel updatedSubCategoryObj = isSubCategoryIdExist.get();
		updatedSubCategoryObj.setSubCategoryName(subCategoryModel.getSubCategoryName());
		updatedSubCategoryObj.setSubCategoryActive(subCategoryModel.getSubCategoryActive());
		updatedSubCategoryObj.setSubCategoryUpdatedAt(ZonedDateTime.now());

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(subCategoryModel.getAuthUserId());
		actionLogData.setAuthUserId(subCategoryModel.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("SubCategory updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		SubCategoryModel updatedData = subCategoryRepository.save(updatedSubCategoryObj);
		return new ApiResponse<>("success", "SubCategory updated successfully.", SubCategoryMapper.toDTO(updatedData));
	}

	@Override
	public ApiResponse<SubCategoryDTO> deleteSubCategory(long authUserId, long subCategoryId) {
		Optional<SubCategoryModel> isSubCategoryIdExist = subCategoryRepository.findById(subCategoryId);

		if (isSubCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("SubCategory deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		subCategoryRepository.deleteById(subCategoryId);
		return new ApiResponse<>("success", "SubCategory deleted successfully.", null);
	}

}
