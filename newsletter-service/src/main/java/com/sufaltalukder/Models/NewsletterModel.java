package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "newsletter_toggle")
	private NewsletterToggle newsletterToggle;

	public enum NewsletterToggle {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant newsletterCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant newsletterUpdatedAt;
}
