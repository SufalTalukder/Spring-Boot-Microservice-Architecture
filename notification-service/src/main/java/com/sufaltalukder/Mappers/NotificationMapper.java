package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Models.NotificationModel;

public class NotificationMapper {

	public static NotificationDTO toDTO(NotificationModel entity) {

		if (entity == null) {
			return null;
		}

		return new NotificationDTO(entity.getNotificationId(), entity.getAuthUserId(),
				entity.getUserId(), entity.getNotificationTitle(),
				entity.getNotificationDescription(), entity.getMarkAsRead(), entity.getNotificationCreatedAt());
	}
}
