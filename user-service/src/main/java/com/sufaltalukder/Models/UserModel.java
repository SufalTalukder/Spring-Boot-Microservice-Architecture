package com.sufaltalukder.Models;

import java.sql.Date;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_tbl", indexes = { @Index(name = "idx_user_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_user_email", columnList = "email_address"),
		@Index(name = "idx_user_phone", columnList = "phone_number"),
		@Index(name = "idx_user_referral_code", columnList = "user_referral_code"),
		@Index(name = "idx_user_active", columnList = "active"),
		@Index(name = "idx_user_created_at", columnList = "created_at") })
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Column(name = "email_address", nullable = false, unique = true)
	private String emailAddress;

	@Column(name = "dob")
	private Date dob;

	@JsonIgnore
	@Column(name = "user_image")
	private String userImage;

	@Column(name = "user_address", columnDefinition = "TEXT")
	private String userAddress;

	@Column(name = "user_referral_code", unique = true)
	private String userReferralCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "active", nullable = false)
	private UserActive userActive;

	public enum UserActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant userCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant userUpdatedAt;
}
