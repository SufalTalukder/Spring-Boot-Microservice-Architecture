package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.DTOs.AuthUserRequest;
import com.sufaltalukder.Models.AuthUserModel;

public class AuthUserMapper {

	public static AuthUserDTO toDTO(AuthUserModel entity) {
		if (entity == null) {
			return null;
		}

		return new AuthUserDTO(entity.getAuthUserId(), entity.getActionByUserInfo(), entity.getAuthUserName(),
				entity.getAuthUserEmailAddress(), entity.getAuthUserPassword(), entity.getAuthUserPhoneNumber(),
				entity.getAuthUserImage(), entity.getAuthUserActive(), entity.getAuthUserType(),
				entity.getAuthUserCreatedAt(), entity.getAuthUserUpdatedAt());
	}

	public static AuthUserModel toEntity(AuthUserDTO dto) {
		if (dto == null) {
			return null;
		}

		AuthUserModel entity = new AuthUserModel();

		entity.setAuthUserId(dto.getAuthUserId());
		entity.setActionByUserInfo(dto.getActionByUserInfo());
		entity.setAuthUserName(dto.getAuthUserName());
		entity.setAuthUserEmailAddress(dto.getAuthUserEmailAddress());
		entity.setAuthUserPassword(dto.getAuthUserPassword());
		entity.setAuthUserPhoneNumber(dto.getAuthUserPhoneNumber());
		entity.setAuthUserImage(dto.getAuthUserImage());
		entity.setAuthUserActive(dto.getAuthUserActive());
		entity.setAuthUserType(dto.getAuthUserType());
		entity.setAuthUserCreatedAt(dto.getAuthUserCreatedAt());
		entity.setAuthUserUpdatedAt(dto.getAuthUserUpdatedAt());

		return entity;
	}

	public static AuthUserModel toEntity(AuthUserRequest dto) {
		if (dto == null) {
			return null;
		}

		AuthUserModel entity = new AuthUserModel();

		entity.setAuthUserId(dto.getAuthUserId());
		entity.setActionByUserInfo(dto.getActionByUserInfo());
		entity.setAuthUserName(dto.getAuthUserName());
		entity.setAuthUserEmailAddress(dto.getAuthUserEmailAddress());
		entity.setAuthUserPassword(dto.getAuthUserPassword());
		entity.setAuthUserPhoneNumber(dto.getAuthUserPhoneNumber());
		entity.setAuthUserActive(dto.getAuthUserActive());
		entity.setAuthUserType(dto.getAuthUserType());
		entity.setAuthUserCreatedAt(dto.getAuthUserCreatedAt());
		entity.setAuthUserUpdatedAt(dto.getAuthUserUpdatedAt());

		return entity;
	}
}
