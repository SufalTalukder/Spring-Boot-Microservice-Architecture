package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.SubCategoryModel.SubCategoryActive;

import lombok.Data;

@Data
public class RequestSubCategoryDTO {

	private String subCategoryName;
	private SubCategoryActive subCategoryActive;

}
