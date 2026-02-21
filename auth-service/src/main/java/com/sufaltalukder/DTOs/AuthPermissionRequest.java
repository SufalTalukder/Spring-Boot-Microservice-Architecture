package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.AuthPermissionModel.PermissionStatus;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class AuthPermissionRequest {

	private long authPermissionId;

	@NotNull(message = "Add permission is required.")
	private PermissionStatus addPermission;

	@NotNull(message = "View all permission is required.")
	private PermissionStatus viewAllPermission;

	@NotNull(message = "View permission is required.")
	private PermissionStatus viewPermission;

	@NotNull(message = "Edit permission is required.")
	private PermissionStatus editPermission;

	@NotNull(message = "Delete permission is required.")
	private PermissionStatus deletePermission;
}
