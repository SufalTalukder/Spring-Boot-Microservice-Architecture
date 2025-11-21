package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.UserModel;

public class UserMapper {

	public static UserDTO toDTO(UserModel entity) {
		if (entity == null) {
			return null;
		}

		return new UserDTO(entity.getUserId(), entity.getAuthUserId(), entity.getFullName(), entity.getPhoneNumber(),
				entity.getEmailAddress(), entity.getDob(), entity.getUserImage(), entity.getUserAddress(),
				entity.getUserReferralCode(), entity.getUserActive(), entity.getUserCreatedAt(),
				entity.getUserUpdatedAt());
	}

	public static UserModel toEntity(UserDTO dto) {
		if (dto == null) {
			return null;
		}

		UserModel entity = new UserModel();

		entity.setUserId(dto.getUserId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setFullName(dto.getFullName());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setEmailAddress(dto.getEmailAddress());
		entity.setDob(dto.getDob());
		entity.setUserImage(dto.getUserImage());
		entity.setUserAddress(dto.getUserAddress());
		entity.setUserReferralCode(dto.getUserReferralCode());
		entity.setUserActive(dto.getUserActive());
		entity.setUserCreatedAt(dto.getUserCreatedAt());
		entity.setUserUpdatedAt(dto.getUserUpdatedAt());

		return entity;
	}
}
