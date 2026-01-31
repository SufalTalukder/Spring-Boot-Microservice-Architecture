package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category_tbl")
public class CategoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long categoryId;

	@Column(name = "auth_user_id", nullable = false)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", referencedColumnName = "authUserId", insertable = false, updatable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "category_name", nullable = false)
	private String categoryName;

	@Column(name = "category_image")
	private String categoryImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "category_status", nullable = false)
	private CategoryActive categoryActive;

	public enum CategoryActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime categoryCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime categoryUpdatedAt;
}
