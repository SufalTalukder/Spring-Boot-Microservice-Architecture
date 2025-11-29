package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notification_tbl")
public class NotificationModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long notificationId;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_Id")
	private long userId;

	@Column(name = "notification_product_id")
	private long notificationProductId;

	@Column(name = "notification_product_img")
	private String notificationProductImg;

	@Column(name = "notification_product_title")
	private String notificationProductTitle;

	@Lob
	@Column(name = "notification_product_description", columnDefinition = "LONGTEXT")
	private String notificationProductDescription;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime notificationCreatedAt;
}
