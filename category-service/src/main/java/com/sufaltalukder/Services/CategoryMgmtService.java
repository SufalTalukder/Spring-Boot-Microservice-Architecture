package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CategoryModel;

public interface CategoryMgmtService {

	ApiResponse<CategoryDTO> createCategory(long authUserId, CategoryModel categoryModel);

	ApiResponse<CategoryDTO> getCategory(long authUserId, long categoryId);

	ApiResponse<List<CategoryDTO>> getAllCategories();

	ApiResponse<CategoryDTO> updateCategory(long authUserId, long categoryId, CategoryModel categoryModel);

	ApiResponse<CategoryDTO> deleteCategory(long authUserId, long categoryId);

	ApiResponse<CategoryDTO> getCategoryByName(String categoryName);

}
