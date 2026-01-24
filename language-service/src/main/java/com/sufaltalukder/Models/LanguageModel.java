package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "language_tbl", indexes = { @Index(name = "idx_language_auth_user", columnList = "auth_user_id"),
		@Index(name = "idx_language_name", columnList = "language_name"),
		@Index(name = "idx_language_user_status", columnList = "auth_user_id, language_status"),
		@Index(name = "idx_language_created_at", columnList = "created_at") })
public class LanguageModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long languageId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "language_name", nullable = false)
	private String languageName;

	@Enumerated(EnumType.STRING)
	@Column(name = "language_status", nullable = false)
	private LanguageActive languageActive;

	public enum LanguageActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant languageCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant languageUpdatedAt;
}
