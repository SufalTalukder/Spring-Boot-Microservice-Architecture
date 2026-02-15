package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.CategoryModel.CategoryActive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

	private long categoryId;
	private String categoryName;
	private CategoryActive categoryActive;

}
