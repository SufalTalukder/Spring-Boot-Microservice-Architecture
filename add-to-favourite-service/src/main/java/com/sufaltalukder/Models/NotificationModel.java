package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notification_mgmt_tbl")
public class NotificationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long notificationId;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "notification_title", nullable = false)
	private String notificationTitle;

	@Lob
	@Column(name = "notification_description", columnDefinition = "LONGTEXT", nullable = false)
	private String notificationDescription;

	@Enumerated(EnumType.STRING)
	@Column(name = "mark_as_read", nullable = false)
	private MarkAsRead markAsRead;

	public enum MarkAsRead {
		READ, UNREAD
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant notificationCreatedAt;
}
