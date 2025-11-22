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

		return new AppBannerDTO(entity.getAppBannerId(), entity.getAuthUserId(), entity.getAppBannerImage(),
				entity.getAppBannerCreatedAt(), entity.getAppBannerUpdatedAt());
	}

	public static AppBannerModel toEntity(AppBannerDTO dto) {
		if (dto == null) {
			return null;
		}

		AppBannerModel entity = new AppBannerModel();

		entity.setAppBannerId(dto.getAppBannerId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setAppBannerImage(dto.getAppBannerImage());
		entity.setAppBannerCreatedAt(dto.getAppBannerCreatedAt());
		entity.setAppBannerUpdatedAt(dto.getAppBannerUpdatedAt());

		return entity;
	}

	public static List<AppBannerDTO> toDTO(List<AppBannerModel> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream().map(AppBannerMapper::toDTO).collect(Collectors.toList());
	}
}
