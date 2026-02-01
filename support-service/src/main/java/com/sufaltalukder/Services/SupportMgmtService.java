package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.RequestSupportDTO;
import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface SupportMgmtService {

	ApiResponse<SupportDTO> addUserSupport(long authUserId, long userId, RequestSupportDTO requestSupportDTO);

	ApiResponse<SupportDTO> getUserSupportDetails(long authUserId, long userId, long supportId);

	ApiResponse<List<SupportDTO>> getAllUserSupports(String supportStatus);

	ApiResponse<SupportDTO> updateUserSupportDetails(long authUserId, long supportId, long userId,
			RequestSupportDTO requestSupportDTO);

	ApiResponse<SupportDTO> deleteUserSupportDetails(long authUserId, long userId, long supportId);

}
