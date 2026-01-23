package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.AppBannerDTO;
import com.sufaltalukder.DTOs.RequestBannerDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface AppBannerMgmtService {

	ApiResponse<List<AppBannerDTO>> uploadMulipleImages(long authUserId, RequestBannerDTO requestBannerDTO);

	ApiResponse<List<AppBannerDTO>> fetchAllBannerImages();

	ApiResponse<List<String>> deleteMultipleBannerImages(long authUserId, List<Long> appBannerIds);

}
