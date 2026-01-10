package com.sufaltalukder.Services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.CategoryDTO;
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

	@Override
	public ApiResponse<CategoryDTO> createCategory(long authUserId, CategoryModel categoryModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		Optional<CategoryModel> isCategoryNameExist = categoryRepository
				.findByCategoryName(categoryModel.getCategoryName());

		if (isCategoryNameExist.isPresent()) {
			return new ApiResponse<>("exist", "Category already exist!", null);
		}

		CategoryModel saveData = new CategoryModel();
		saveData.setAuthUserInfo(authUser);
		saveData.setCategoryName(categoryModel.getCategoryName());
		saveData.setCategoryActive(categoryModel.getCategoryActive());

		CategoryModel savedData = categoryRepository.save(saveData);

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Category created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Category created successfully.", CategoryMapper.toDTO(savedData));
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
	public ApiResponse<CategoryDTO> updateCategory(long authUserId, long categoryId, CategoryModel categoryModel) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		CategoryModel categoryToUpdate = isCategoryIdExist.get();

		if (!categoryToUpdate.getCategoryName().equals(categoryModel.getCategoryName())) {
			Optional<CategoryModel> isCategoryNameExist = categoryRepository
					.findByCategoryName(categoryModel.getCategoryName());

			if (isCategoryNameExist.isPresent()) {
				return new ApiResponse<>("exist", "Category already exist!", null);
			}
		}

		categoryToUpdate.setAuthUserInfo(authUser);
		categoryToUpdate.setCategoryName(categoryModel.getCategoryName());
		categoryToUpdate.setCategoryActive(categoryModel.getCategoryActive());

		CategoryModel updatedData = categoryRepository.save(categoryToUpdate);

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
		Optional<CategoryModel> isCategoryNameExist = categoryRepository.findByCategoryName(categoryName);

		if (isCategoryNameExist.isPresent()) {
			return new ApiResponse<>("success", "Category found.", CategoryMapper.toDTO(isCategoryNameExist.get()));
		} else {
			return new ApiResponse<>("not found", "Category not found.", null);
		}
	}
}