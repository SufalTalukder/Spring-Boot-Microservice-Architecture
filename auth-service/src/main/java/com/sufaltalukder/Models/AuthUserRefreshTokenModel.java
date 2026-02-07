package com.sufaltalukder.Models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "auth_user_refresh_token")
public class AuthUserRefreshTokenModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refreshTokenId;

	@Column(name = "auth_user_id", nullable = false)
	private Long authUserId;

	@Column(name = "refresh_token", nullable = false, unique = true, length = 500)
	private String refreshToken;

	@Column(name = "expiry_date", nullable = false)
	private Instant expiryDate;

	@Column(name = "is_revoked", nullable = false)
	private boolean isRevoked;
}
