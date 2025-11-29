package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Models.NotificationModel;

public class NotificationMapper {

	public static NotificationDTO toDTO(NotificationModel entity) {
		if (entity == null) {
			return null;
		}

		return new NotificationDTO(entity.getNotificationId(), entity.getAuthUserId(), entity.getUserId(),
				entity.getNotificationProductId(), entity.getNotificationProductImg(),
				entity.getNotificationProductTitle(), entity.getNotificationProductDescription(),
				entity.getNotificationCreatedAt());
	}

	public static NotificationModel toEntity(NotificationDTO dto) {
		if (dto == null) {
			return null;
		}

		NotificationModel entity = new NotificationModel();

		entity.setNotificationId(dto.getNotificationId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setUserId(dto.getUserId());
		entity.setNotificationProductId(dto.getNotificationProductId());
		entity.setNotificationProductImg(dto.getNotificationProductImg());
		entity.setNotificationProductTitle(dto.getNotificationProductTitle());
		entity.setNotificationProductDescription(dto.getNotificationProductDescription());
		entity.setNotificationCreatedAt(dto.getNotificationCreatedAt());

		return entity;
	}
}
