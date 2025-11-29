package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Mappers.NotificationMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Repositories.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public ApiResponse<NotificationDTO> pushInAppNotification(NotificationModel notificationModel) {

		NotificationModel saveData = notificationRepository.save(notificationModel);

		return new ApiResponse<>("success", "Notification pushed successfully.", NotificationMapper.toDTO(saveData));

	}

	@Override
	public ApiResponse<List<NotificationDTO>> getAllPushedInAppNotifications(long userId) {

		List<NotificationModel> isUserNotificationsExist = notificationRepository.findAllNotificationsByUserId(userId);

		if (isUserNotificationsExist == null) {
			return new ApiResponse<>("not found", "No notification(s) found.", null);
		}

		List<NotificationDTO> allNotificationsOfUser = isUserNotificationsExist.stream().map(NotificationMapper::toDTO)
				.toList();

		return new ApiResponse<List<NotificationDTO>>("success", "All notification(s) of user fetched successfully.",
				allNotificationsOfUser);
	}

	@Override
	public ApiResponse<NotificationDTO> removeInAppNotification(long userId, long notificationId) {

		NotificationModel isNotificationIdExist = notificationRepository.findByNotificationId(notificationId);

		if (isNotificationIdExist == null) {
			return new ApiResponse<>("not found", "No notification ID found.", null);
		}

		notificationRepository.deleteById(notificationId);

		return new ApiResponse<>("success", "Notification deleted successfully.", null);
	}
}