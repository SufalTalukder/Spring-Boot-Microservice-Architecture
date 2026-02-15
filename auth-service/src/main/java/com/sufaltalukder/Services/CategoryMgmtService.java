package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CategoryModel;

public interface CategoryMgmtService {

	ApiResponse<CategoryDTO> createCategory(CategoryModel categoryModel);
	
	ApiResponse<CategoryDTO> getCategory(long categoryId);

	ApiResponse<List<CategoryDTO>> getAllCategories();

	ApiResponse<CategoryDTO> updateCategory(long categoryId, CategoryModel categoryModel);

	ApiResponse<CategoryDTO> deleteCategory(long categoryId);

	ApiResponse<CategoryDTO> getCategoryByName(String categoryName);

}
