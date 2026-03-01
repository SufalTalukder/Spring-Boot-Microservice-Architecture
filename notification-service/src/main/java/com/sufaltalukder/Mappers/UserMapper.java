package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.UserModel;

public class UserMapper {

	public static UserDTO toDTO(UserModel entity) {
		if (entity == null) {
			return null;
		}

		return new UserDTO(entity.getUserId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()), entity.getFullName(),
				entity.getPhoneNumber(), entity.getEmailAddress(), entity.getDob(), entity.getUserImage(),
				entity.getUserAddress(), entity.getUserReferralCode(), entity.getUserActive(),
				entity.getUserCreatedAt(), entity.getUserUpdatedAt());
	}
}
