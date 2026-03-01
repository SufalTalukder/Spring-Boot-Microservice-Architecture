package com.sufaltalukder.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class NotificationRequest {

	private long authUserId;
	private long userId;

	@NotBlank(message = "Notification title is required.")
	private String notificationTitle;

	@NotBlank(message = "Notification description is required.")
	private String notificationDescription;

}
