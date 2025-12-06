package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.UserRatingDTO;
import com.sufaltalukder.Models.UserRatingModel;

public class UserRatingMapper {

	public static UserRatingDTO toDTO(UserRatingModel entity) {
		if (entity == null) {
			return null;
		}

		return new UserRatingDTO(entity.getUserRatingId(), entity.getAuthUserId(), entity.getUserId(),
				entity.getUserInfo(), entity.getProductId(), entity.getUserRating(), entity.getUserComment(),
				entity.getUserRatingCreatedAt(), entity.getUserRatingUpdatedAt());
	}

	public static UserRatingModel toEntity(UserRatingDTO dto) {
		if (dto == null) {
			return null;
		}

		UserRatingModel entity = new UserRatingModel();

		entity.setUserRatingId(dto.getUserRatingId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setUserId(dto.getUserId());
		entity.setProductId(dto.getProductId());
		entity.setUserRating(dto.getUserRating());
		entity.setUserComment(dto.getUserComment());

		return entity;
	}
}
