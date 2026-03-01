package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.DTOs.NotificationRequest;
import com.sufaltalukder.Models.ApiResponse;

import jakarta.validation.Valid;

public interface NotificationMgmtService {

	ApiResponse<NotificationDTO> pushMgmtNotification(@Valid NotificationRequest notificationRequest);

	ApiResponse<List<NotificationDTO>> getAllMgmtNotifications();

	ApiResponse<NotificationDTO> markAsReadMgmtNotification(long authUserId, long notificationId);

	ApiResponse<NotificationDTO> removeMgmtNotification(long notificationId);

}
