package com.sufaltalukder.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.sufaltalukder.DTOs.AppBannerDTO;
import com.sufaltalukder.Models.AppBannerModel;

public class AppBannerMapper {

	public static AppBannerDTO toDTO(AppBannerModel entity) {
		if (entity == null) {
			return null;
		}

		return new AppBannerDTO(entity.getAppBannerId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				entity.getAppBannerImage(), entity.getBannerActive(), entity.getAppBannerCreatedAt(),
				entity.getAppBannerUpdatedAt());
	}

	public static List<AppBannerDTO> toDTO(List<AppBannerModel> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream().map(AppBannerMapper::toDTO).collect(Collectors.toList());
	}
}
