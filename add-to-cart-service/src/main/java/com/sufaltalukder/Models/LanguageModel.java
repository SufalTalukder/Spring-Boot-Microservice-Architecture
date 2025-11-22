package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "language_tbl")
public class LanguageModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long languageId;

	@Column(name = "auth_user_Id", nullable = false)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", referencedColumnName = "authUserId", insertable = false, updatable = false)
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
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime languageCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime languageUpdatedAt;
}
