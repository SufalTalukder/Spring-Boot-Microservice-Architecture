package com.sufaltalukder.Models;

import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_login_audit_tbl", indexes = { @Index(name = "idx_auth_login_user", columnList = "auth_user_id"),
		@Index(name = "idx_auth_login_created_at", columnList = "created_at"),
		@Index(name = "idx_auth_login_user_created", columnList = "auth_user_id, created_at"),
		@Index(name = "idx_auth_login_ip", columnList = "ip_address"),
		@Index(name = "idx_auth_login_status", columnList = "login_status") })
public class AuthLoginAuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auth_login_audit_id")
	private long authLoginAuditId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = true)
	private AuthUserModel authUserInfo;

	@Column(name = "ip_address", length = 45)
	private String ipAddress;

	@Column(name = "user_agent", columnDefinition = "TEXT", nullable = true)
	private String userAgent;

	@Column(name = "browser", length = 50)
	private String browser;

	@Column(name = "browser_version", length = 20)
	private String browserVersion;

	@Column(name = "operating_system", length = 50)
	private String operatingSystem;

	@Column(name = "os_version", length = 20)
	private String osVersion;

	@Column(name = "device_type", length = 20)
	private String deviceType;

	@Column(name = "device_model", length = 50)
	private String deviceModel;

	@Column(name = "possible_incognito")
	private Boolean possibleIncognito;

	@Column(name = "login_status", length = 20, nullable = false)
	private String loginStatus;

	@Column(name = "auth_method", length = 30)
	private String authMethod;

	@Column(name = "failure_reason", length = 50)
	private String failureReason;

	@Column(name = "session_id", length = 100)
	private String sessionId;

	@Column(name = "referrer_url", length = 255)
	private String referrerUrl;

	@Column(name = "login_time", nullable = false)
	private Instant loginTime;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant createdAt;
}
