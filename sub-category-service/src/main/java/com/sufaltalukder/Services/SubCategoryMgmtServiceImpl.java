package com.sufaltalukder.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.RequestSubCategoryDTO;
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

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<SubCategoryDTO> createSubCategory(long authUserId, RequestSubCategoryDTO requestSubCategoryDTO,
			MultipartFile subCategoryImage) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		SubCategoryModel isSubCategoryNameExist = subCategoryRepository
				.findBySubCategoryName(requestSubCategoryDTO.getSubCategoryName());

		if (isSubCategoryNameExist != null) {
			return new ApiResponse<>("exist", "SubCategory already exists!", null);
		}

		SubCategoryModel savedData = new SubCategoryModel();
		savedData.setAuthUserInfo(authUser);
		savedData.setSubCategoryName(requestSubCategoryDTO.getSubCategoryName());
		savedData.setSubCategoryActive(requestSubCategoryDTO.getSubCategoryActive());

		SubCategoryModel saveData = subCategoryRepository.save(savedData);

		// Upload image using newly created subCategoryId
		if (subCategoryImage != null && !subCategoryImage.isEmpty()) {
			String imageName = storeAuthUserImage(savedData.getSubCategoryId(), subCategoryImage);
			savedData.setSubCategoryImage(imageName);
			subCategoryRepository.save(savedData);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("SubCategory added successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "SubCategory added successfully.", SubCategoryMapper.toDTO(saveData));
	}

	private String storeAuthUserImage(long categoryId, MultipartFile file) {
		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = categoryId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

			Path filePath = uploadPath.resolve(fileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return fileName;

		} catch (IOException e) {
			throw new RuntimeException("Failed to store subcategory image", e);
		}
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
			RequestSubCategoryDTO requestSubCategoryDTO, MultipartFile subCategoryImage) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		Optional<SubCategoryModel> isSubCategoryIdExist = subCategoryRepository.findById(subCategoryId);

		if (isSubCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}

		SubCategoryModel isSubCategoryNameExist = subCategoryRepository
				.findBySubCategoryName(requestSubCategoryDTO.getSubCategoryName());

		if (isSubCategoryNameExist != null && isSubCategoryNameExist.getSubCategoryId() != subCategoryId) {
			return new ApiResponse<>("exist", "SubCategory name already used!", null);
		}

		SubCategoryModel updatedSubCategoryObj = isSubCategoryIdExist.get();
		updatedSubCategoryObj.setAuthUserInfo(authUser);
		updatedSubCategoryObj.setSubCategoryName(requestSubCategoryDTO.getSubCategoryName());
		updatedSubCategoryObj.setSubCategoryActive(requestSubCategoryDTO.getSubCategoryActive());

		SubCategoryModel updatedData = subCategoryRepository.save(updatedSubCategoryObj);

		// Upload image using newly created subCategoryId
		if (subCategoryImage != null && !subCategoryImage.isEmpty()) {
			String imageName = storeAuthUserImage(updatedData.getSubCategoryId(), subCategoryImage);
			updatedData.setSubCategoryImage(imageName);
			subCategoryRepository.save(updatedData);
		}

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

	@Override
	public ApiResponse<SubCategoryDTO> getSubCategoryName(String subCategoryName) {

		SubCategoryModel isCategoryNameExist = subCategoryRepository.findBySubCategoryName(subCategoryName);

		if (isCategoryNameExist != null) {
			return new ApiResponse<>("success", "SubCategory found.", SubCategoryMapper.toDTO(isCategoryNameExist));
		} else {
			return new ApiResponse<>("not found", "SubCategory not found.", null);
		}
	}
}
