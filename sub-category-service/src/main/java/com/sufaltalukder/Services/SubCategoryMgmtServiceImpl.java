package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Mappers.SubCategoryMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.SubCategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.SubCategoryRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class SubCategoryMgmtServiceImpl implements SubCategoryMgmtService {

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Override
	public ApiResponse<SubCategoryDTO> createSubCategory(long authUserId, SubCategoryModel subCategoryModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		SubCategoryModel isSubCategoryNameExist = subCategoryRepository
				.findBySubCategoryName(subCategoryModel.getSubCategoryName());

		if (isSubCategoryNameExist != null) {
			return new ApiResponse<>("exist", "SubCategory already exist!", null);
		}

		SubCategoryModel savedData = new SubCategoryModel();
		savedData.setAuthUserInfo(authUser);
		savedData.setSubCategoryName(subCategoryModel.getSubCategoryName());
		savedData.setSubCategoryActive(subCategoryModel.getSubCategoryActive());
		savedData.setSubCategoryCreatedAt(ZonedDateTime.now());

		SubCategoryModel saveData = subCategoryRepository.save(savedData);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("SubCategory added successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "SubCategory added successfully.", SubCategoryMapper.toDTO(saveData));
	}

	@Override
	public ApiResponse<List<SubCategoryDTO>> getAllSubCategories() {

		List<SubCategoryModel> fetchedAllData = subCategoryRepository.findAllSubCategories();

		if (fetchedAllData.isEmpty()) {
			return new ApiResponse<>("not found", "No subcategori(s) found.", null);
		}

		List<SubCategoryDTO> dtos = fetchedAllData.stream().map(SubCategoryMapper::toDTO).toList();

		return new ApiResponse<>("success", "All subcategorie(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<SubCategoryDTO> getSubCategory(long authUserId, long subCategoryId) {

		Optional<SubCategoryModel> isCategoryIdExist = subCategoryRepository.findSubcategoryByIdOfAuth(subCategoryId);

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
	public ApiResponse<SubCategoryDTO> updateSubCategory(long authUserId, long subCategoryId,
			SubCategoryModel subCategoryModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

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
		updatedSubCategoryObj.setAuthUserInfo(authUser);
		updatedSubCategoryObj.setSubCategoryName(subCategoryModel.getSubCategoryName());
		updatedSubCategoryObj.setSubCategoryActive(subCategoryModel.getSubCategoryActive());
		updatedSubCategoryObj.setSubCategoryUpdatedAt(ZonedDateTime.now());

		SubCategoryModel updatedData = subCategoryRepository.save(updatedSubCategoryObj);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("SubCategory updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "SubCategory updated successfully.", SubCategoryMapper.toDTO(updatedData));
	}

	@Override
	public ApiResponse<SubCategoryDTO> deleteSubCategory(long authUserId, long subCategoryId) {

		Optional<SubCategoryModel> isSubCategoryIdExist = subCategoryRepository.findById(subCategoryId);

		if (isSubCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		subCategoryRepository.deleteById(subCategoryId);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("SubCategory deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "SubCategory deleted successfully.", null);
	}

}
