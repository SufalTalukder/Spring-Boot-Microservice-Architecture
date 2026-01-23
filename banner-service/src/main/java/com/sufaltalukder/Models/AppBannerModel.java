package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "app_banner_tbl", indexes = { @Index(name = "idx_app_banner_active", columnList = "active"),
		@Index(name = "idx_app_banner_created_at", columnList = "created_at"),
		@Index(name = "idx_app_banner_auth_user", columnList = "auth_user_id") })
public class AppBannerModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long appBannerId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "app_banner_image", nullable = false)
	private String appBannerImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "active", nullable = false)
	private BannerActive bannerActive;

	public enum BannerActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant appBannerCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant appBannerUpdatedAt;
}
