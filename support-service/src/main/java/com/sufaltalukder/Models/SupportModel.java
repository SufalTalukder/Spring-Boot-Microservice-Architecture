package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "support_tbl", indexes = { @Index(name = "idx_support_auth_user", columnList = "auth_user_id"),
		@Index(name = "idx_support_user", columnList = "user_id"),
		@Index(name = "idx_support_status", columnList = "support_status"),
		@Index(name = "idx_support_created_at", columnList = "created_at"),
		@Index(name = "idx_support_updated_at", columnList = "updated_at"),
		@Index(name = "idx_user_status", columnList = "user_id, support_status"),
		@Index(name = "idx_status_created", columnList = "support_status, created_at") })
public class SupportModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long supportId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserModel userInfo;

	@Lob
	@Column(name = "support_text", columnDefinition = "LONGTEXT", nullable = false)
	private String supportText;

	@Enumerated(EnumType.STRING)
	@Column(name = "support_status", nullable = false)
	private SupportStatus supportStatus;

	public enum SupportStatus {
		PENDING, ON_GOING, RESOLVED
	}

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant supportCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant supportUpdatedAt;
}
