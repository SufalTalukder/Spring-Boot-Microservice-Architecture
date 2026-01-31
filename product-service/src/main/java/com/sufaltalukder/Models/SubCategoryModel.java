package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sub_category_tbl")
public class SubCategoryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long subCategoryId;

	@Column(name = "auth_user_id", nullable = false)
	private long authUserId;

	@ManyToOne
	@JoinColumn(name = "auth_user_id", referencedColumnName = "authUserId", insertable = false, updatable = false)
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
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime subCategoryCreatedAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private ZonedDateTime subCategoryUpdatedAt;
}
