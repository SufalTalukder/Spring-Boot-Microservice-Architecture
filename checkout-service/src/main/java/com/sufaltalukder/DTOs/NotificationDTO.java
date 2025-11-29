package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

	private long notificationId;
	private long authUserId;
	private long userId;
	private long notificationProductId;
	private String notificationProductImg;
	private String notificationProductTitle;
	private String notificationProductDescription;
	private ZonedDateTime notificationCreatedAt;

}
