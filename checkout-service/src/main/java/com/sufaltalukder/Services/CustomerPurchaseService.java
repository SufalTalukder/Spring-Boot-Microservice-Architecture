package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.CheckOutHistoryDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.PaginationApiResponse;

public interface CustomerPurchaseService {

	ApiResponse<CheckOutHistoryDTO> createUserCheckOut(CheckOutHistoryModel checkOutHistoryModel);

	ApiResponse<CheckOutHistoryDTO> getPurchaseDetails(long userId, long checkOutHistoryId);

	PaginationApiResponse<List<CheckOutHistoryDTO>> getAllPurchasesList(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	ApiResponse<CheckOutHistoryDTO> cancelPurchase(long userId, long checkOutHistoryId);

}