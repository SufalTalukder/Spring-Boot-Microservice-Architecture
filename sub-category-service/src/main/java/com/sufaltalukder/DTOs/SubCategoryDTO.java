package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.SubCategoryModel.subCategoryActive;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {

	private long subCategoryId;
	private AuthUserModel authUserInfo;
	private String subCategoryName;
	private String subCategoryImage;
	private subCategoryActive subCategoryActive;
	private ZonedDateTime subCategoryCreatedAt;
	private ZonedDateTime subCategoryUpdatedAt;

}
