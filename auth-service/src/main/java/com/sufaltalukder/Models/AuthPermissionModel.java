package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_permission_tbl", indexes = {
		@Index(name = "idx_auth_permission_user_id", columnList = "auth_user_id") })
public class AuthPermissionModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long authPermissionId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "action_by_user_id", nullable = false)
	private AuthUserModel actionByUserInfo;

	@Enumerated(EnumType.STRING)
	@Column(name = "add_permission", nullable = false)
	private PermissionStatus addPermission;

	@Enumerated(EnumType.STRING)
	@Column(name = "view_all_permission", nullable = false)
	private PermissionStatus viewAllPermission;

	@Enumerated(EnumType.STRING)
	@Column(name = "view_permission", nullable = false)
	private PermissionStatus viewPermission;

	@Enumerated(EnumType.STRING)
	@Column(name = "edit_permission", nullable = false)
	private PermissionStatus editPermission;

	@Enumerated(EnumType.STRING)
	@Column(name = "delete_permission", nullable = false)
	private PermissionStatus deletePermission;

	public enum PermissionStatus {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant authPermissionCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant authPermissionUpdatedAt;
}
