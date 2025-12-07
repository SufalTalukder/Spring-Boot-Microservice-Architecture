package com.sufaltalukder.Models;

import java.sql.Date;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_tbl")
public class UserModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(name = "auth_user_Id")
	private long authUserId;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "user_image")
	private String userImage;

	@Column(name = "user_address")
	private String userAddress;

	@Column(name = "user_referral_code", nullable = true)
	private String userReferralCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "active")
	private UserActive userActive;

	public enum UserActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime userCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime userUpdatedAt;
}
