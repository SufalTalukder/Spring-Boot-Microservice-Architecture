package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.Models.AuthUserModel;

public class AuthUserMapper {

	public static AuthUserDTO toDTO(AuthUserModel entity) {
		if (entity == null) {
			return null;
		}

		return new AuthUserDTO(entity.getAuthUserId(), entity.getAuthUserName(), entity.getAuthUserEmailAddress(),
				entity.getAuthUserPassword(), entity.getAuthUserPhoneNumber(), entity.getAuthUserImage(),
				entity.getActionByUserId(), entity.getAuthUserActive(), entity.getAuthUserType(),
				entity.getAuthUserCreatedAt(), entity.getAuthUserUpdatedAt());
	}

	public static AuthUserModel toEntity(AuthUserDTO dto) {
		if (dto == null) {
			return null;
		}

		AuthUserModel entity = new AuthUserModel();

		entity.setAuthUserId(dto.getAuthUserId());
		entity.setAuthUserName(dto.getAuthUserName());
		entity.setAuthUserEmailAddress(dto.getAuthUserEmailAddress());
		entity.setAuthUserPassword(dto.getAuthUserPassword());
		entity.setAuthUserPhoneNumber(dto.getAuthUserPhoneNumber());
		entity.setAuthUserImage(dto.getAuthUserImage());
		entity.setActionByUserId(dto.getActionByUserId());
		entity.setAuthUserActive(dto.getAuthUserActive());
		entity.setAuthUserType(dto.getAuthUserType());
		entity.setAuthUserCreatedAt(dto.getAuthUserCreatedAt());
		entity.setAuthUserUpdatedAt(dto.getAuthUserUpdatedAt());

		return entity;
	}
}
