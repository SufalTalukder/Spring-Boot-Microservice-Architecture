package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;

public interface NotificationService {

	ApiResponse<NotificationDTO> pushInAppNotification(NotificationModel notificationModel);

	ApiResponse<List<NotificationDTO>> getAllPushedInAppNotifications(long userId);

	ApiResponse<NotificationDTO> removeInAppNotification(long userId, long notificationId);

}
