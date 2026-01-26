package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.RequestSubCategoryDTO;
import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface SubCategoryMgmtService {

	ApiResponse<SubCategoryDTO> createSubCategory(long authUserId, RequestSubCategoryDTO requestSubCategoryDTO,
			MultipartFile subCategoryImage);

	ApiResponse<List<SubCategoryDTO>> getAllSubCategories();

	ApiResponse<SubCategoryDTO> getSubCategory(long authUserId, long subCategoryId);

	ApiResponse<SubCategoryDTO> updateSubCategory(long authUserId, long subCategoryId,
			RequestSubCategoryDTO requestSubCategoryDTO, MultipartFile subCategoryImage);

	ApiResponse<SubCategoryDTO> deleteSubCategory(long authUserId, long subCategoryId);

	ApiResponse<SubCategoryDTO> getSubCategoryName(String subCategoryName);

}
