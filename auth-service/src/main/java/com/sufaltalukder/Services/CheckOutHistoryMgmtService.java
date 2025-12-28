package com.sufaltalukder.Services;

import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;

public interface CheckOutHistoryMgmtService {

	ApiResponse<CheckOutHistoryDTO> createUserCheckOut(CheckOutHistoryModel checkOutHistoryModel);

}
