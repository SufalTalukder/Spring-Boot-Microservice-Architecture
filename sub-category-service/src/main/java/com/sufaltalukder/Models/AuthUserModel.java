package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_tbl", indexes = { @Index(name = "idx_auth_user_email", columnList = "auth_user_email"),
		@Index(name = "idx_auth_user_phone", columnList = "auth_user_phone_number"),
		@Index(name = "idx_auth_user_status", columnList = "auth_user_status"),
		@Index(name = "idx_auth_user_type", columnList = "auth_user_type"),
		@Index(name = "idx_action_by_user", columnList = "action_by_user_id"),
		@Index(name = "idx_email_status", columnList = "auth_user_email, auth_user_status") })
public class AuthUserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "action_by_user_id")
	@JsonBackReference
	private AuthUserModel actionByUserInfo;

	@Column(name = "auth_user_name", nullable = false)
	private String authUserName;

	@Column(name = "auth_user_email", nullable = false, unique = true)
	private String authUserEmailAddress;

	@Column(name = "auth_user_password")
	private String authUserPassword;

	@Column(name = "auth_user_phone_number", unique = true)
	private String authUserPhoneNumber;

	@Column(name = "auth_user_image")
	private String authUserImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_user_status", nullable = false)
	private AuthUserActive authUserActive;

	public enum AuthUserActive {
		YES, NO
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_user_type", nullable = false)
	private AuthUserType authUserType;

	public enum AuthUserType {
		SUPER_ADMIN, ADMIN
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant authUserCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant authUserUpdatedAt;
}
