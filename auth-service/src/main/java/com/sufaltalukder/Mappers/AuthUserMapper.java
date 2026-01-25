package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.DTOs.AuthUserRequest;
import com.sufaltalukder.DTOs.AuthUserShallowDTO;
import com.sufaltalukder.Models.AuthUserModel;

public class AuthUserMapper {

	public static AuthUserDTO toDTO(AuthUserModel entity) {
		if (entity == null) {
			return null;
		}

		return new AuthUserDTO(entity.getAuthUserId(), toShallowDTO(entity.getActionByUserInfo()),
				entity.getAuthUserName(), entity.getAuthUserEmailAddress(), entity.getAuthUserPhoneNumber(),
				entity.getAuthUserImage(), entity.getAuthUserActive(), entity.getAuthUserType(),
				entity.getAuthUserCreatedAt(), entity.getAuthUserUpdatedAt());
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

	private static AuthUserShallowDTO toShallowDTO(AuthUserModel entity) {
		if (entity == null)
			return null;
		return new AuthUserShallowDTO(entity.getAuthUserId(), entity.getAuthUserName());
	}
}
