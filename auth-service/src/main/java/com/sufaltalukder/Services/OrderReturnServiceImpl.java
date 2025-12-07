package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.OrderReturnDTO;
import com.sufaltalukder.Mappers.OrderReturnMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.sufaltalukder.Models.OrderReturnModel;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.OrderReturnModel.IsReturn;
import com.sufaltalukder.Models.OrderReturnModel.ReturnAmountStatus;
import com.sufaltalukder.Repositories.CheckOutHistoryRepository;
import com.sufaltalukder.Repositories.OrderReturnRepository;

@Service
public class OrderReturnServiceImpl implements OrderReturnService {

	@Autowired
	private OrderReturnRepository orderReturnRepository;

	@Autowired
	private CheckOutHistoryRepository checkOutHistoryRepository;

	@Override
	public ApiResponse<OrderReturnDTO> createUserOrderReturn(OrderReturnModel orderReturnModel) {
		Optional<CheckOutHistoryModel> checkOutHistoryOpt = checkOutHistoryRepository
				.findById(orderReturnModel.getCheckOutHistoryId());

		if (checkOutHistoryOpt.isEmpty()) {
			return new ApiResponse<>("not found", "Checkout ID not found.", null);
		}

		Optional<OrderReturnModel> existingReturn = orderReturnRepository
				.findByCheckOutHistoryId(orderReturnModel.getCheckOutHistoryId());

		if (existingReturn.isPresent()) {
			return new ApiResponse<>("exist", "Order return already exists for this checkout ID.", null);
		}

		CheckOutHistoryModel checkOutHistory = checkOutHistoryOpt.get();

		OrderReturnModel storeData = new OrderReturnModel();
		storeData.setCheckOutHistoryId(checkOutHistory.getCheckOutHistoryId());
		storeData.setAuthUserId(orderReturnModel.getAuthUserId());
		storeData.setUserId(checkOutHistory.getUserId());
		storeData.setIsReturn(IsReturn.YES);
		storeData.setReturnInDays(orderReturnModel.getReturnInDays());
		storeData.setReturnAmount(checkOutHistory.getPaymentAmount());
		storeData.setReturnAmountStatus(ReturnAmountStatus.PENDING);

		OrderReturnModel savedData = orderReturnRepository.save(storeData);

		return new ApiResponse<>("success", "Order return created successfully.", OrderReturnMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<OrderReturnDTO> getUserOrderReturn(long orderReturnId) {
		Optional<OrderReturnModel> isOrderReturnIdExist = orderReturnRepository.findById(orderReturnId);

		if (isOrderReturnIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Order return ID not found.", null);
		}

		return new ApiResponse<>("success", "Order return fetched successfully.",
				OrderReturnMapper.toDTO(isOrderReturnIdExist.get()));
	}

	@Override
	public PaginationApiResponse<List<OrderReturnDTO>> getUserAllOrderReturns(long userId, int pageNo, int pageSize,
			String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		Page<OrderReturnModel> orderReturnsOfUser = orderReturnRepository.findByUserId(userId, pageable);

		if (orderReturnsOfUser.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No order return(s) list found for user ID: " + userId,
					null, 0, 0, 0);
		}

		List<OrderReturnDTO> dtos = orderReturnsOfUser.stream().map(OrderReturnMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "User order return(s) fetched successfully.", dtos,
				orderReturnsOfUser.getNumber() + 1, orderReturnsOfUser.getSize(),
				orderReturnsOfUser.getTotalElements());
	}

	@Override
	public PaginationApiResponse<List<OrderReturnDTO>> getAllUserOrderReturn(int pageNo, int pageSize) {

		Page<OrderReturnModel> orderReturns = orderReturnRepository.findAll(PageRequest.of(pageNo - 1, pageSize));

		if (orderReturns.isEmpty()) {
			return new PaginationApiResponse<>("not found", "Order return(s) not found.", null, 0, 0, 0);
		}

		List<OrderReturnDTO> dtos = orderReturns.stream().map(OrderReturnMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "All order return(s) fetched successfully.", dtos,
				orderReturns.getNumber() + 1, orderReturns.getSize(), orderReturns.getTotalElements());
	}

	@Override
	public ApiResponse<OrderReturnDTO> updateUserOrderReturn(long orderReturnId, OrderReturnModel orderReturnModel) {
		Optional<OrderReturnModel> isOrderReturnIdExist = orderReturnRepository.findById(orderReturnId);

		if (isOrderReturnIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "Order return ID not found.", null);
		}

		OrderReturnModel orderReturnInfo = isOrderReturnIdExist.get();

		orderReturnInfo.setReturnInDays(orderReturnModel.getReturnInDays());
		orderReturnInfo.setReturnAmountStatus(orderReturnModel.getReturnAmountStatus());
		orderReturnInfo.setReturnAmountDateTime(ZonedDateTime.now());

		OrderReturnModel updatedData = orderReturnRepository.save(orderReturnInfo);

		return new ApiResponse<>("success", "Order return updated successfully.", OrderReturnMapper.toDTO(updatedData));
	}
}
