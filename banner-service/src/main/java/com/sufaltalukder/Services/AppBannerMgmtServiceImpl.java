package com.sufaltalukder.Services;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AppBannerDTO;
import com.sufaltalukder.Mappers.AppBannerMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AppBannerModel;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Repositories.AppBannerRepository;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

@Service
public class AppBannerMgmtServiceImpl implements AppBannerMgmtService {

	@Autowired
	private AppBannerRepository appBannerRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	private final String UPLOAD_DIR = "uploads";

	@Override
	public ApiResponse<List<AppBannerDTO>> uploadMulipleImages(long authUserId, MultipartFile[] appBannerImages) {

		List<AppBannerModel> savedImages = new ArrayList<>();

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		for (MultipartFile file : appBannerImages) {
			if (file.isEmpty()) {
				continue;
			}

			try {
				String originalFileName = file.getOriginalFilename();
				String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
				String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

				Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				AppBannerModel appBannerModel = new AppBannerModel();
				appBannerModel.setAuthUserInfo(authUser);
				appBannerModel.setAppBannerImage(uniqueFileName);

				savedImages.add(appBannerRepository.save(appBannerModel));

			} catch (IOException e) {
				throw new RuntimeException("File upload failed", e);
			}
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Banner image(s) uploaded successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		List<AppBannerDTO> bannerDTOs = AppBannerMapper.toDTO(savedImages);
		return new ApiResponse<>("success", "Banner image(s) uploaded successfully.", bannerDTOs);
	}

	@Override
	public ApiResponse<List<AppBannerDTO>> fetchAllBannerImages() {

		List<AppBannerModel> fetchAllImagesData = appBannerRepository.findAllBanners();

		if (fetchAllImagesData.isEmpty()) {
			return new ApiResponse<>("not found", "No banner image(s) found.", null);
		}

		List<AppBannerDTO> dtos = fetchAllImagesData.stream().map(AppBannerMapper::toDTO).toList();

		return new ApiResponse<>("success", "All banner image(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<List<String>> deleteMultipleBannerImages(long authUserId, List<Long> appBannerIds) {

		List<Long> nonExistentIds = new ArrayList<>();
		List<Long> deletedIds = new ArrayList<>();

		for (Long eachBannerId : appBannerIds) {
			// remove images from the folder
			Path uploadPath = Paths.get(UPLOAD_DIR);
			Optional<AppBannerModel> bannerOptional = appBannerRepository.findById(eachBannerId);
			if (bannerOptional.isPresent()) {
				AppBannerModel banner = bannerOptional.get();
				String imageName = banner.getAppBannerImage();
				if (imageName != null && !imageName.isEmpty()) {
					Path existingImagePath = uploadPath.resolve(imageName);
					try {
						Files.delete(existingImagePath);
					} catch (IOException e) {
						return new ApiResponse<>("error", "Failed to delete existing image: " + e.getMessage(), null);
					}
				}
			}
			if (appBannerRepository.existsById(eachBannerId)) {
				appBannerRepository.deleteById(eachBannerId);
				deletedIds.add(eachBannerId);
			} else {
				nonExistentIds.add(eachBannerId);
			}
		}
		if (!deletedIds.isEmpty() && !nonExistentIds.isEmpty()) {
			return new ApiResponse<>("partial", "Some of the banner images ID(s) do not exists: " + nonExistentIds
					+ " and partially deleted ID(s): " + deletedIds, null);
		}
		if (deletedIds.isEmpty() && !nonExistentIds.isEmpty()) {
			return new ApiResponse<>("not found", "Deleted ID(s) not found: " + nonExistentIds, null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Multiple banner image(s) deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Multiple banner image(s) deleted successfully: " + deletedIds, null);
	}
}
