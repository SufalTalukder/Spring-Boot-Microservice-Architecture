package com.sufaltalukder.Models;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "recent_search_tbl")
public class RecentSearchesModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long recentSearchId;

	@Column(name = "user_id", nullable = false)
	private long userId;

	@Column(name = "searched_value", nullable = false)
	private String searchedValue;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime recentSearchCreatedAt = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
}
