package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthResponseDTO;
import com.sufaltalukder.Models.AuthUserModel;

public class AuthUserMapper {

	public static AuthResponseDTO toDTO(AuthUserModel entity) {
		if (entity == null) {
			return null;
		}

		return new AuthResponseDTO(entity.getAuthUserId(), entity.getAuthUserName(), entity.getAuthUserEmailAddress(),
				entity.getAuthUserPhoneNumber(), entity.getAuthUserImage(), entity.getAuthUserActive(),
				entity.getAuthUserType());
	}
}
