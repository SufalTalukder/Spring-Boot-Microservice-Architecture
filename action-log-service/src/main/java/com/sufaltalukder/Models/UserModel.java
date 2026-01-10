package com.sufaltalukder.Models;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_tbl", indexes = { @Index(name = "idx_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_phone_number", columnList = "phone_number"),
		@Index(name = "idx_email_address", columnList = "email_address"),
		@Index(name = "idx_user_active", columnList = "active"),
		@Index(name = "idx_created_at", columnList = "created_at") })
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(name = "auth_user_id", nullable = false)
	private long authUserId;

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@Column(name = "email_address", unique = true)
	private String emailAddress;

	@Column(name = "dob")
	private LocalDate dob;

	@Column(name = "user_image")
	private String userImage;

	@Column(name = "user_address", length = 500)
	private String userAddress;

	@Column(name = "user_referral_code")
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
