package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthUserDTO;
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

	private static AuthUserShallowDTO toShallowDTO(AuthUserModel entity) {
		if (entity == null)
			return null;
		return new AuthUserShallowDTO(entity.getAuthUserId(), entity.getAuthUserName());
	}
}
