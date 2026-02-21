package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthPermissionDTO;
import com.sufaltalukder.Models.AuthPermissionModel;

public class AuthPermissionMapper {

	public static AuthPermissionDTO toDTO(AuthPermissionModel entity) {

		if (entity == null) {
			return null;
		}

		return new AuthPermissionDTO(entity.getAuthPermissionId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				entity.getActionByUserId(), entity.getAddPermission(), entity.getViewAllPermission(),
				entity.getViewPermission(), entity.getEditPermission(), entity.getDeletePermission(),
				entity.getAuthPermissionCreatedAt(), entity.getAuthPermissionUpdatedAt());
	}
}
