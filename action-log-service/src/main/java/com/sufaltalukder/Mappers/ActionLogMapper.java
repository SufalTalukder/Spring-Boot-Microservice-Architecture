package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;

public class ActionLogMapper {

	public static ActionLogDTO toDTO(ActionLogModel entity) {
		if (entity == null) {
			return null;
		}

		return new ActionLogDTO(entity.getActionLogId(), entity.getActionByAuthUserId(), entity.getAuthUserId(),
				entity.getUserId(), entity.getActionLogMethod(), entity.getActionLogMessage(),
				entity.getActionLogCreatedAt());
	}

	public static ActionLogModel toEntity(ActionLogDTO dto) {
		if (dto == null) {
			return null;
		}

		ActionLogModel entity = new ActionLogModel();

		entity.setActionLogId(dto.getActionLogId());
		entity.setActionByAuthUserId(dto.getActionByAuthUserId());
		entity.setAuthUserId(dto.getAuthUserId());
		entity.setUserId(dto.getUserId());
		entity.setActionLogMethod(dto.getActionLogMethod());
		entity.setActionLogMessage(dto.getActionLogMessage());
		entity.setActionLogCreatedAt(dto.getActionLogCreatedAt());

		return entity;
	}
}
