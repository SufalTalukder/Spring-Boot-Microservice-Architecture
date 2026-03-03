package com.sufaltalukder.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.DTOs.NotificationRequest;
import com.sufaltalukder.Mappers.NotificationMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Models.NotificationModel.MarkAsRead;
import com.sufaltalukder.Repositories.NotificationRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationMgmtServiceImpl implements NotificationMgmtService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public ApiResponse<NotificationDTO> pushMgmtNotification(@Valid NotificationRequest notificationRequest) {

		NotificationModel savedData = new NotificationModel();

		savedData.setAuthUserId(notificationRequest.getAuthUserId());
		savedData.setUserId(notificationRequest.getUserId());
		savedData.setNotificationTitle(notificationRequest.getNotificationTitle());
		savedData.setNotificationDescription(notificationRequest.getNotificationDescription());
		savedData.setMarkAsRead(MarkAsRead.UNREAD);

		return new ApiResponse<>("success", "Notification pushed successfully.",
				NotificationMapper.toDTO(notificationRepository.save(savedData)));
	}

	@Override
	public ApiResponse<List<NotificationDTO>> getAllMgmtNotifications() {

		List<NotificationModel> notifications = notificationRepository.findAllNotifications();

		if (notifications.isEmpty()) {
			return new ApiResponse<>("not found", "No notification(s) found.", null);
		}

		List<NotificationDTO> dtos = notifications.stream().map(NotificationMapper::toDTO).toList();

		return new ApiResponse<>("success", "All notification(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<NotificationDTO> markAsReadMgmtNotification(long authUserId, long notificationId) {

		Optional<NotificationModel> notification = notificationRepository.findByNotificationId(notificationId);

		if (notification.isEmpty()) {
			return new ApiResponse<>("not found", "Notification ID not found.", null);
		}

		NotificationModel updateData = notification.get();
		updateData.setAuthUserId(authUserId);
		updateData.setUserId(0);
		updateData.setMarkAsRead(MarkAsRead.READ);
		
		NotificationModel saveData = notificationRepository.save(updateData);

		return new ApiResponse<>("success", "Notification marked as read successfully.",
				NotificationMapper.toDTO(saveData));
	}

	@Override
	public ApiResponse<NotificationDTO> removeMgmtNotification(long notificationId) {

		Optional<NotificationModel> notification = notificationRepository.findByNotificationId(notificationId);

		if (notification.isEmpty()) {
			return new ApiResponse<>("not found", "Notification ID not found.", null);
		}

		notificationRepository.deleteById(notificationId);

		return new ApiResponse<>("success", "Notification deleted successfully.", null);
	}

}