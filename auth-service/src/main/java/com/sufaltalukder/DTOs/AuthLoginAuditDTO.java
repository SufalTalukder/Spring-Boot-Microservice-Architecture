package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginAuditDTO {

	private long authLoginAuditId;
	private AuthUserModel authUserInfo;
	private String ipAddress;
	private String userAgent;
	private String browser;
	private String browserVersion;
	private String operatingSystem;
	private String osVersion;
	private String deviceType;
	private String deviceModel;
	private boolean possibleIncognito;
	private String loginStatus;
	private String authMethod;
	private String failureReason;
	private Instant loginTime;
	private String sessionId;
	private String referrerUrl;
	private Instant createdAt;
}
