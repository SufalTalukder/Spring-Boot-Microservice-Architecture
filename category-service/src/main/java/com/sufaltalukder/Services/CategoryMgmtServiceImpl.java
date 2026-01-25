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

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.DTOs.RequestCategoryDTO;
import com.sufaltalukder.Mappers.CategoryMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Repositories.CategoryRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class CategoryMgmtServiceImpl implements CategoryMgmtService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<CategoryDTO> createCategory(long authUserId, RequestCategoryDTO requestCategoryDTO,
			MultipartFile categoryImage) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		CategoryModel isCategoryNameExist = categoryRepository.findByCategoryName(requestCategoryDTO.getCategoryName());

		if (isCategoryNameExist != null) {
			return new ApiResponse<>("exist", "Category already exists!", null);
		}

		CategoryModel saveData = new CategoryModel();
		saveData.setAuthUserInfo(authUser);
		saveData.setCategoryName(requestCategoryDTO.getCategoryName());
		saveData.setCategoryActive(requestCategoryDTO.getCategoryActive());

		CategoryModel savedData = categoryRepository.save(saveData);

		// Upload image using newly created categoryId
		if (categoryImage != null && !categoryImage.isEmpty()) {
			String imageName = storeAuthUserImage(savedData.getCategoryId(), categoryImage);
			savedData.setCategoryImage(imageName);
			categoryRepository.save(savedData);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Category created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Category created successfully.", CategoryMapper.toDTO(savedData));
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
			throw new RuntimeException("Failed to store category image", e);
		}
	}

	@Override
	public ApiResponse<CategoryDTO> getCategory(long authUserId, long categoryId) {

		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findCategoryByIdOfAuth(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.GET);
		actionLogData.setActionLogMessage("Category fetched successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Category fetched successfully.",
				CategoryMapper.toDTO(isCategoryIdExist.get()));
	}

	@Override
	public ApiResponse<List<CategoryDTO>> getAllCategories() {

		List<CategoryModel> fetchedAllData = categoryRepository.findAllCategories();

		if (fetchedAllData.isEmpty()) {
			return new ApiResponse<>("not found", "Categorie(s) not found.", null);
		}

		List<CategoryDTO> dtos = fetchedAllData.stream().map(CategoryMapper::toDTO).toList();

		return new ApiResponse<>("success", "All categorie(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<CategoryDTO> updateCategory(long authUserId, long categoryId,
			RequestCategoryDTO requestCategoryDTO, MultipartFile categoryImage) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		CategoryModel exists = categoryRepository.findByCategoryName(requestCategoryDTO.getCategoryName());

		if (exists != null && exists.getCategoryId() != categoryId) {
			return new ApiResponse<>("exist", "Category already exists!", null);
		}

		CategoryModel categoryToUpdate = isCategoryIdExist.get();

		categoryToUpdate.setAuthUserInfo(authUser);
		categoryToUpdate.setCategoryName(requestCategoryDTO.getCategoryName());
		categoryToUpdate.setCategoryActive(requestCategoryDTO.getCategoryActive());

		CategoryModel updatedData = categoryRepository.save(categoryToUpdate);

		// Upload image using newly created categoryId
		if (categoryImage != null && !categoryImage.isEmpty()) {
			String imageName = storeAuthUserImage(updatedData.getCategoryId(), categoryImage);
			updatedData.setCategoryImage(imageName);
			categoryRepository.save(updatedData);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.PUT);
		actionLogData.setActionLogMessage("Category updated successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Category updated successfully.", CategoryMapper.toDTO(updatedData));
	}

	@Override
	public ApiResponse<CategoryDTO> deleteCategory(long authUserId, long categoryId) {
		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		categoryRepository.deleteById(categoryId);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Category deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Category deleted successfully.", null);
	}

	@Override
	public ApiResponse<CategoryDTO> getCategoryByName(String categoryName) {
		CategoryModel isCategoryNameExist = categoryRepository.findByCategoryName(categoryName);

		if (isCategoryNameExist != null) {
			return new ApiResponse<>("success", "Category found.", CategoryMapper.toDTO(isCategoryNameExist));
		} else {
			return new ApiResponse<>("not found", "Category not found.", null);
		}
	}
}