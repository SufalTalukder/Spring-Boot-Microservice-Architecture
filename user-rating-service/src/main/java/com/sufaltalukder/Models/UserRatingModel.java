package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "user_rating_tbl")
public class UserRatingModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userRatingId;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId", insertable = false, updatable = false)
	private UserModel userInfo;

	@Column(name = "product_id")
	private long productId;

	@Column(name = "user_rating")
	private int userRating;

	@Lob
	@Column(name = "user_comment", columnDefinition = "LONGTEXT")
	private String userComment;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime userRatingCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime userRatingUpdatedAt;
}
