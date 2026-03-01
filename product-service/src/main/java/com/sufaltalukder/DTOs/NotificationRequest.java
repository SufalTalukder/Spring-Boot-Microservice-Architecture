package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class NotificationRequest {

	private AuthUserModel authUserInfo;

	private UserModel userInfo;

	@NotBlank(message = "Notification title is required.")
	private String notificationTitle;

	@NotBlank(message = "Notification description is required.")
	private String notificationDescription;

}
