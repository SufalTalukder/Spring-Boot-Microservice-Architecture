package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_banner_tbl")
public class AppBannerModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long appBannerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "app_banner_image")
	private String appBannerImage;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime appBannerCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime appBannerUpdatedAt;
}
