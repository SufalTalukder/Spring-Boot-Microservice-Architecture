package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.AuthUserModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppBannerDTO {

	private long appBannerId;
	private AuthUserModel authUserInfo;
	private String appBannerImage;
	private ZonedDateTime appBannerCreatedAt;
	private ZonedDateTime appBannerUpdatedAt;
}
