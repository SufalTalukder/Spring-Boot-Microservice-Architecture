package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "newsletter_tbl")
public class NewsletterModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long newsletterId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id")
	private AuthUserModel authUserInfo;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserModel userInfo;

	@Column(name = "newsletter_toggle")
	private String newsletterToggle;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime newsletterCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime newsletterUpdatedAt;
}
