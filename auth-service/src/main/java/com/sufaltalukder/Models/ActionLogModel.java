package com.sufaltalukder.Models;

import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "action_log_tbl")
public class ActionLogModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long actionLogId;

	@Column(name = "action_by_auth_user_id", nullable = true)
	private long actionByAuthUserId;

	@Column(name = "auth_user_id", nullable = true)
	private long authUserId;

	@Column(name = "user_id", nullable = true)
	private long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "action_log_method")
	private ActionLogMethod actionLogMethod;

	public enum ActionLogMethod {
		POST, GET, PUT, PATCH, DELETE
	}

	@Column(name = "action_log_message")
	private String actionLogMessage;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime actionLogCreatedAt;

}