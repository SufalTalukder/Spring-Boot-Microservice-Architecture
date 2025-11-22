package com.sufaltalukder.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "otp_tbl")
public class OtpModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long otpId;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId", insertable = false, updatable = false)
	private UserModel userInfo;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "otp")
	private String otp;

	@Column(name = "otp_verified")
	private boolean otpVerified;

	@Column(name = "otp_expired")
	private LocalDateTime otpExpired;
}
