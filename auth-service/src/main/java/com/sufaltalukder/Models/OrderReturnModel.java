package com.sufaltalukder.Models;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_return_tbl")
public class OrderReturnModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderReturnId;

	@Column(name = "check_out_history_id", nullable = false)
	private long checkOutHistoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "check_out_history_id", referencedColumnName = "checkOutHistoryId", insertable = false, updatable = false)
	private CheckOutHistoryModel checkOutHistoryInfo;

	@Column(name = "auth_user_id")
	private long authUserId;

	@Column(name = "user_id")
	private long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "is_return")
	private IsReturn isReturn;

	public enum IsReturn {
		YES
	}

	@Column(name = "return_in_days", nullable = false)
	private String returnInDays;

	@Column(name = "return_amount")
	private double returnAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "return_amount_status", nullable = false)
	private ReturnAmountStatus returnAmountStatus;

	public enum ReturnAmountStatus {
		COMPLETED, PENDING, FAILED
	}

	@UpdateTimestamp
	@Column(name = "return_amount_datetime")
	private ZonedDateTime returnAmountDateTime;

	@CreationTimestamp
	@Column(name = "created_at", updatable = false)
	private ZonedDateTime orderReturnCreatedAt;
}
