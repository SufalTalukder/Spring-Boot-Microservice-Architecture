package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.SubCategoryModel;

public interface SubCategoryMgmtService {

	ApiResponse<SubCategoryDTO> createSubCategory(SubCategoryModel subCategoryModel);

	ApiResponse<List<SubCategoryDTO>> getAllSubCategories();

	ApiResponse<SubCategoryDTO> getSubCategory(long authUserId, long subCategoryId);

	ApiResponse<SubCategoryDTO> updateSubCategory(long subCategoryId, SubCategoryModel subCategoryModel);

	ApiResponse<SubCategoryDTO> deleteSubCategory(long authUserId, long subCategoryId);

}
