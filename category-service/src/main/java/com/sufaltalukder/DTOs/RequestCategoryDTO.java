package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.CategoryModel.CategoryActive;

import lombok.Data;

@Data
public class RequestCategoryDTO {

	private String categoryName;
	private CategoryActive categoryActive;

}
