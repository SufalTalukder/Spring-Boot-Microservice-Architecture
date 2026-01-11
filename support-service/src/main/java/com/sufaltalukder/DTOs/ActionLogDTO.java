package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLogDTO {

	private long actionLogId;
	private long actionByAuthUserId;
	private long authUserId;
	private long userId;
	private String actionLogMethod;
	private String actionLogMessage;
	private ZonedDateTime actionLogCreatedAt;

}
