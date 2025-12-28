package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.OrderReturnDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.OrderReturnModel;
import com.sufaltalukder.Models.PaginationApiResponse;

public interface OrderReturnService {

	ApiResponse<OrderReturnDTO> createUserOrderReturn(OrderReturnModel orderReturnModel);

	ApiResponse<OrderReturnDTO> getUserOrderReturn(long orderReturnId);

	PaginationApiResponse<List<OrderReturnDTO>> getAllUserOrderReturn(int pageNo, int pageSize);

	PaginationApiResponse<List<OrderReturnDTO>> getUserAllOrderReturns(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir);

	ApiResponse<OrderReturnDTO> updateUserOrderReturn(long orderReturnId, OrderReturnModel orderReturnModel);

}
