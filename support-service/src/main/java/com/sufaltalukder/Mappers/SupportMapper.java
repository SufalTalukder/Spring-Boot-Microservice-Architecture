package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Models.SupportModel;

public class SupportMapper {

	public static SupportDTO toDTO(SupportModel entity) {
		if (entity == null) {
			return null;
		}

		return new SupportDTO(entity.getSupportId(), entity.getAuthUserInfo(), entity.getUserInfo(),
				entity.getSupportText(), entity.getSupportStatus(), entity.getSupportCreatedAt(),
				entity.getSupportUpdatedAt());
	}

	public static SupportModel toEntity(SupportDTO dto) {
		if (dto == null) {
			return null;
		}

		SupportModel entity = new SupportModel();

		entity.setSupportId(dto.getSupportId());
		entity.setAuthUserInfo(dto.getAuthUserInfo());
		entity.setUserInfo(dto.getUserInfo());
		entity.setSupportText(dto.getSupportText());
		entity.setSupportStatus(dto.getSupportStatus());
		entity.setSupportCreatedAt(dto.getSupportCreatedAt());
		entity.setSupportUpdatedAt(dto.getSupportUpdatedAt());

		return entity;
	}
}
