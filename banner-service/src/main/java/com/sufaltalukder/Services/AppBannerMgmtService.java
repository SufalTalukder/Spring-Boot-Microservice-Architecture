package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AppBannerDTO;
import com.sufaltalukder.Models.ApiResponse;

public interface AppBannerMgmtService {

	ApiResponse<List<AppBannerDTO>> uploadMulipleImages(long authUserId, MultipartFile[] appBannerImage);

	ApiResponse<List<AppBannerDTO>> fetchAllBannerImages();

	ApiResponse<List<String>> deleteMultipleBannerImages(long authUserId, List<Long> appBannerIds);

}
