package com.sufaltalukder.Models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sub_category_tbl", indexes = { @Index(name = "idx_sub_category_name", columnList = "sub_category_name"),
		@Index(name = "idx_sub_category_status", columnList = "sub_category_status"),
		@Index(name = "idx_auth_user_id", columnList = "auth_user_id"),
		@Index(name = "idx_created_at", columnList = "created_at") })
public class SubCategoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long subCategoryId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", nullable = false)
	private AuthUserModel authUserInfo;

	@Column(name = "sub_category_name", nullable = false)
	private String subCategoryName;

	@Column(name = "sub_category_image")
	private String subCategoryImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "sub_category_status", nullable = false)
	private SubCategoryActive subCategoryActive;

	public enum SubCategoryActive {
		YES, NO
	}

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "TIMESTAMP", updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private java.time.Instant subCategoryCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "TIMESTAMP")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
	private java.time.Instant subCategoryUpdatedAt;
}
