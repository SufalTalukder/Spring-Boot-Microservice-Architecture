package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.CategoryModel.CategoryActive;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

	private long categoryId;
	private long authUserId;
	private AuthUserModel authUserInfo;
	private String categoryName;
	private String categoryImage;
	private CategoryActive categoryActive;
	private ZonedDateTime categoryCreatedAt;
	private ZonedDateTime categoryUpdatedAt;
}
