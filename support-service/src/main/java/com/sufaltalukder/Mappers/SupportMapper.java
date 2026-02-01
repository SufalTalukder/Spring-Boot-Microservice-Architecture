package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Models.SupportModel;

public class SupportMapper {

	public static SupportDTO toDTO(SupportModel entity) {
		if (entity == null) {
			return null;
		}

		return new SupportDTO(entity.getSupportId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				UserMapper.toDTO(entity.getUserInfo()), entity.getSupportText(), entity.getSupportStatus(),
				entity.getSupportCreatedAt(), entity.getSupportUpdatedAt());
	}
}
