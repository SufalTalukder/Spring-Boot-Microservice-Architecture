package com.sufaltalukder.Models;

import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_tbl")
public class AuthUserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long authUserId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "action_by_user_id")
	@JsonBackReference
	private AuthUserModel actionByUserInfo;

	@Column(name = "auth_user_name", nullable = false)
	private String authUserName;

	@Column(name = "auth_user_email", nullable = false)
	private String authUserEmailAddress;

	@Column(name = "auth_user_password")
	private String authUserPassword;

	@Column(name = "auth_user_phone_number")
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
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime authUserCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime authUserUpdatedAt;
}