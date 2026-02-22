package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthPermissionDTO;
import com.sufaltalukder.DTOs.AuthUserShallowDTO;
import com.sufaltalukder.Models.AuthPermissionModel;
import com.sufaltalukder.Models.AuthUserModel;

public class AuthPermissionMapper {

	public static AuthPermissionDTO toDTO(AuthPermissionModel entity) {

		if (entity == null) {
			return null;
		}

		return new AuthPermissionDTO(entity.getAuthPermissionId(), AuthUserMapper.toDTO(entity.getAuthUserInfo()),
				toShallowDTO(entity.getActionByUserInfo()), entity.getAddPermission(), entity.getViewAllPermission(),
				entity.getViewPermission(), entity.getEditPermission(), entity.getDeletePermission(),
				entity.getAuthPermissionCreatedAt(), entity.getAuthPermissionUpdatedAt());
	}

	private static AuthUserShallowDTO toShallowDTO(AuthUserModel entity) {
		if (entity == null)
			return null;
		return new AuthUserShallowDTO(entity.getAuthUserId(), entity.getAuthUserName());
	}
}
