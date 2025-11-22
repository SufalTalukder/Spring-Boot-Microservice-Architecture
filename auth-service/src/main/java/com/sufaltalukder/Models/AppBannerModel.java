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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long appBannerId;

	@Column(name = "auth_user_id", nullable = false)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", referencedColumnName = "authUserId", insertable = false, updatable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "app_banner_image", nullable = false)
	private String appBannerImage;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime appBannerCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime appBannerUpdatedAt;
}
