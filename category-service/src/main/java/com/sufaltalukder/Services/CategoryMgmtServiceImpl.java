package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.Mappers.CategoryMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CategoryModel;
import com.sufaltalukder.Repositories.CategoryRepository;

@Service
public class CategoryMgmtServiceImpl implements CategoryMgmtService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public ApiResponse<CategoryDTO> createCategory(CategoryModel categoryModel) {
		Optional<CategoryModel> isCategoryNameExist = categoryRepository
				.findByCategoryName(categoryModel.getCategoryName());

		if (isCategoryNameExist.isPresent()) {
			return new ApiResponse<>("exist", "Category already exist!", null);
		}

		CategoryModel savedData = categoryRepository.save(categoryModel);
		return new ApiResponse<>("success", "Category created successfully.", CategoryMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<CategoryDTO> getCategory(long categoryId) {
		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		return new ApiResponse<>("success", "Category fetched successfully.",
				CategoryMapper.toDTO(isCategoryIdExist.get()));
	}

	@Override
	public ApiResponse<List<CategoryDTO>> getAllCategories() {
		List<CategoryModel> fetchedAllData = categoryRepository.findAll();

		if (fetchedAllData.isEmpty()) {
			return new ApiResponse<>("not found", "Categorie(s) not found.", null);
		}

		List<CategoryDTO> dtos = fetchedAllData.stream().map(CategoryMapper::toDTO).toList();

		return new ApiResponse<>("success", "All categories fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<CategoryDTO> updateCategory(long categoryId, CategoryModel categoryModel) {
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

		categoryToUpdate.setCategoryName(categoryModel.getCategoryName());
		categoryToUpdate.setCategoryActive(categoryModel.getCategoryActive());
		categoryToUpdate.setCategoryUpdatedAt(ZonedDateTime.now());

		CategoryModel updatedData = categoryRepository.save(categoryToUpdate);
		return new ApiResponse<>("success", "Category updated successfully.", CategoryMapper.toDTO(updatedData));
	}

	@Override
	public ApiResponse<CategoryDTO> deleteCategory(long categoryId) {
		Optional<CategoryModel> isCategoryIdExist = categoryRepository.findById(categoryId);

		if (isCategoryIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Category not found.", null);
		}

		categoryRepository.deleteById(categoryId);
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