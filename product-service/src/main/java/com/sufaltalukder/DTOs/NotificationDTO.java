package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.NotificationModel.MarkAsRead;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

	private long notificationId;
	private AuthUserDTO authUserInfo;
	private UserDTO userInfo;
	private String notificationTitle;
	private String notificationDescription;
	private MarkAsRead markAsRead;
	private Instant notificationCreatedAt;

}
