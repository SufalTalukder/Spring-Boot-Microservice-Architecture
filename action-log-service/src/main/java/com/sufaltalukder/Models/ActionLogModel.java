package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "action_log_tbl", indexes = {
		@Index(name = "idx_action_by_auth_user", columnList = "action_by_auth_user_id"),
		@Index(name = "idx_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_user_id", columnList = "user_id"),
		@Index(name = "idx_action_method", columnList = "action_log_method"),
		@Index(name = "idx_created_at", columnList = "created_at") })
public class ActionLogModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long actionLogId;

	@Column(name = "action_by_auth_user_id")
	private long actionByAuthUserId;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "action_log_method", nullable = false)
	private ActionLogMethod actionLogMethod;

	public enum ActionLogMethod {
		POST, GET, PUT, PATCH, DELETE
	}

	@Column(name = "action_log_message", length = 500)
	private String actionLogMessage;

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant actionLogCreatedAt;
}
