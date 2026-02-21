package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthPermissionModel.PermissionStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthPermissionDTO {

	private long authPermissionId;
	private AuthUserDTO authUserInfo;
	private long actionByUserId;
	private PermissionStatus addPermission;
	private PermissionStatus viewAllPermission;
	private PermissionStatus viewPermission;
	private PermissionStatus editPermission;
	private PermissionStatus deletePermission;
	private Instant authPermissionCreatedAt;
	private Instant authPermissionUpdatedAt;

}
