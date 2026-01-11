package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.SupportDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.SupportModel;

public interface SupportMgmtService {

	ApiResponse<SupportDTO> addUserSupport(long authUserId, long userId, SupportModel supportModel);

	ApiResponse<SupportDTO> getUserSupportDetails(long authUserId, long userId, long supportId);

	ApiResponse<List<SupportDTO>> getAllUserSupports();

	ApiResponse<SupportDTO> updateUserSupportDetails(long authUserId, long supportId, long userId,
			SupportModel supportModel);

	ApiResponse<SupportDTO> deleteUserSupportDetails(long authUserId, long userId, long supportId);

}
