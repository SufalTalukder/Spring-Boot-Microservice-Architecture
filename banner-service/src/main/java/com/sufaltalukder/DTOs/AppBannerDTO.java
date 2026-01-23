package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AppBannerModel.BannerActive;

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
	private BannerActive bannerActive;
	private Instant appBannerCreatedAt;
	private Instant appBannerUpdatedAt;
}
