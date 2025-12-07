package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.OrderReturnModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnDTO {

	private long orderReturnId;
	private long checkOutHistoryId;
	private long authUserId;
	private long userId;
	private OrderReturnModel.IsReturn isReturn;
	private String returnInDays;
	private double returnAmount;
	private OrderReturnModel.ReturnAmountStatus returnAmountStatus;
	private ZonedDateTime returnAmountDateTime;
	private ZonedDateTime orderReturnCreatedAt;
}
