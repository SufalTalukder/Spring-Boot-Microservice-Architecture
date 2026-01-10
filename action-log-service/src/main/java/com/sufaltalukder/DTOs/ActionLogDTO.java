package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLogDTO {

	private long actionLogId;
	private long actionByAuthUserId;
	private long authUserId;
	private long userId;
	private ActionLogMethod actionLogMethod;
	private String actionLogMessage;
	private Instant actionLogCreatedAt;

}
