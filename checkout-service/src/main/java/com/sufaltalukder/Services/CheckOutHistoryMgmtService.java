package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;

public interface CheckOutHistoryMgmtService {

	ApiResponse<CheckOutDTO> createUserCheckOut(long authUserId, long userId,
			CheckOutHistoryModel checkOutHistoryModel);

	ApiResponse<List<CheckOutHistoryDTO>> getAllCheckOutHistories();

}
