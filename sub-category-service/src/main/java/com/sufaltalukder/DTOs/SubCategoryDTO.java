package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.SubCategoryModel.SubCategoryActive;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {

	private long subCategoryId;
	private AuthUserModel authUserInfo;
	private String subCategoryName;
	private String subCategoryImage;
	private SubCategoryActive subCategoryActive;
	private Instant subCategoryCreatedAt;
	private Instant subCategoryUpdatedAt;

}
