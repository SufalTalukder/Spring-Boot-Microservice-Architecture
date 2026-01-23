package com.sufaltalukder.DTOs;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.Models.AppBannerModel.BannerActive;

import lombok.Data;

@Data
public class RequestBannerDTO {

	private List<MultipartFile> appBannerImages;
	private BannerActive bannerActive;
}
