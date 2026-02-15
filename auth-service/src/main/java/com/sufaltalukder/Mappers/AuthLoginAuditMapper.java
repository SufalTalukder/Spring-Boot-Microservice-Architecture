package com.sufaltalukder.Mappers;

import com.sufaltalukder.DTOs.AuthLoginAuditDTO;
import com.sufaltalukder.Models.AuthLoginAuditModel;

public class AuthLoginAuditMapper {

	public static AuthLoginAuditDTO toDTO(AuthLoginAuditModel entity) {
		if (entity == null) {
			return null;
		}

		return new AuthLoginAuditDTO(entity.getAuthLoginAuditId(), entity.getAuthUserInfo(), entity.getIpAddress(),
				entity.getUserAgent(), entity.getBrowser(), entity.getBrowserVersion(), entity.getOperatingSystem(),
				entity.getOsVersion(), entity.getDeviceType(), entity.getDeviceModel(), entity.getPossibleIncognito(),
				entity.getLoginStatus(), entity.getAuthMethod(), entity.getFailureReason(), entity.getLoginTime(),
				entity.getSessionId(), entity.getReferrerUrl(), entity.getCreatedAt());
	}
}
