package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.SubCategoryModel.SubCategoryActive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryResponseDTO {

	private long subCategoryId;
	private String subCategoryName;
	private SubCategoryActive subCategoryActive;

}
