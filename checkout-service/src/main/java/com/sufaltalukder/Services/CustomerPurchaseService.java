package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CheckOutDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.PaginationApiResponse;

public interface CustomerPurchaseService {

	ApiResponse<CheckOutDTO> createUserCheckOut(long userId, CheckOutHistoryModel checkOutHistoryModel);

	ApiResponse<CheckOutDTO> getPurchaseDetails(long userId, long checkOutHistoryId);

	PaginationApiResponse<List<CheckOutDTO>> getAllPurchasesList(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	ApiResponse<CheckOutDTO> cancelPurchase(long userId, long checkOutHistoryId);

}