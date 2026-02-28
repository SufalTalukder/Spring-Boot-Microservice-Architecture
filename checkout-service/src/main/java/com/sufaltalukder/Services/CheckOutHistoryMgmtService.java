package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryRequest;
import com.sufaltalukder.Models.ApiResponse;

public interface CheckOutHistoryMgmtService {

	ApiResponse<CheckOutDTO> createUserCheckOut(long authUserId, long userId,
			CheckOutHistoryRequest checkOutHistoryRequest);

	ApiResponse<List<CheckOutHistoryDTO>> getAllCheckOutHistories();

	ApiResponse<CheckOutHistoryDTO> getCheckoutDetails(long authUserId, long checkOutHistoryId);

}
