package com.sufaltalukder.feign.Services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sufaltalukder.DTOs.NotificationDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.NotificationModel;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationFeignService {

	@PostMapping("/api/v1/elastic/user/push-inapp-notification")
	ApiResponse<NotificationDTO> pushInAppNotification(@RequestBody NotificationModel notificationModel);
}
