package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.UserResponseDTO;
import com.sufaltalukder.Models.UserModel;

public class UserMapper {

	public static UserResponseDTO toDTO(UserModel entity) {
		if (entity == null) {
			return null;
		}

		return new UserResponseDTO(entity.getUserId(), entity.getFullName(), entity.getPhoneNumber(),
				entity.getEmailAddress(), entity.getDob(), entity.getUserImage(), entity.getUserAddress(),
				entity.getUserReferralCode(), entity.getUserActive());
	}
}
