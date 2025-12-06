package com.sufaltalukder.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Mappers.NotificationMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;
import com.sufaltalukder.Models.ProductModel;
import com.sufaltalukder.Repositories.NotificationRepository;
import com.sufaltalukder.Repositories.ProductRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ApiResponse<NotificationDTO> pushInAppNotification(NotificationModel notificationModel) {

		Optional<ProductModel> productOpt = productRepository
				.findByProductId(notificationModel.getNotificationProductId());

		if (productOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Product ID not found.", null);
		}

		ProductModel product = productOpt.get();

		// push notification product details
		notificationModel.setUserId(notificationModel.getUserId());
		notificationModel.setNotificationProductId(product.getProductId());
		notificationModel.setNotificationProductTitle(product.getProductName());
		notificationModel.setNotificationProductDescription(product.getProductDetails());
		notificationModel.setNotificationProductImg(product.getProductImage());

		NotificationModel saved = notificationRepository.save(notificationModel);

		return new ApiResponse<>("success", "Notification pushed successfully.", NotificationMapper.toDTO(saved));
	}

	@Override
	public ApiResponse<List<NotificationDTO>> getAllPushedInAppNotifications(long userId) {

		List<NotificationModel> notifications = notificationRepository.findAllNotificationsByUserId(userId);

		if (notifications.isEmpty()) {
			return new ApiResponse<>("not found", "No notification(s) found.", null);
		}

		List<NotificationDTO> dtos = notifications.stream().map(NotificationMapper::toDTO).toList();

		return new ApiResponse<>("success", "All notification(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<NotificationDTO> removeInAppNotification(long userId, long notificationId) {

		NotificationModel notification = notificationRepository.findByNotificationId(notificationId);

		if (notification == null) {
			return new ApiResponse<>("not found", "Notification ID not found.", null);
		}

		// verify that notification belongs to this user
		if (notification.getUserId() != userId) {
			return new ApiResponse<>("error", "Unauthorized — cannot delete others' notifications.", null);
		}

		notificationRepository.deleteById(notificationId);

		return new ApiResponse<>("success", "Notification deleted successfully.", null);
	}

}