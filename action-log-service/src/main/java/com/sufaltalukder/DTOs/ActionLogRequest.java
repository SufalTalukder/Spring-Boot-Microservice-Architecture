package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActionLogRequest {

	private long actionByAuthUserId;
	private long authUserId;
	private long userId;

	@NotNull(message = "Action method is required")
	private ActionLogMethod actionLogMethod;

	@NotBlank(message = "Action log is required")
	private String actionLogMessage;
}
