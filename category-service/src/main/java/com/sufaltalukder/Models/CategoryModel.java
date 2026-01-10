package com.sufaltalukder.Models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category_tbl", indexes = { @Index(name = "idx_category_name", columnList = "category_name"),
		@Index(name = "idx_category_status", columnList = "category_status"),
		@Index(name = "idx_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_created_at", columnList = "created_at") })
public class CategoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long categoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auth_user_id", nullable = false)
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
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant categoryCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private Instant categoryUpdatedAt;
}
