package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.DTOs.RequestCategoryDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface CategoryMgmtService {

	ApiResponse<CategoryDTO> createCategory(long authUserId, RequestCategoryDTO requestCategoryDTO,
			MultipartFile categoryImage);

	ApiResponse<CategoryDTO> getCategory(long authUserId, long categoryId);

	ApiResponse<List<CategoryDTO>> getAllCategories();

	ApiResponse<CategoryDTO> updateCategory(long authUserId, long categoryId, RequestCategoryDTO requestCategoryDTO,
			MultipartFile categoryImage);

	ApiResponse<CategoryDTO> deleteCategory(long authUserId, long categoryId);

	ApiResponse<CategoryDTO> getCategoryByName(String categoryName);

}
