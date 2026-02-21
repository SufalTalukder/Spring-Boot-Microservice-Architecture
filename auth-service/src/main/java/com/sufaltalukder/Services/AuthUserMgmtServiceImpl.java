package com.sufaltalukder.Services;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AuthLoginAuditDTO;
import com.sufaltalukder.DTOs.AuthPermissionDTO;
import com.sufaltalukder.DTOs.AuthPermissionRequest;
import com.sufaltalukder.DTOs.AuthResponseDTO;
import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.DTOs.AuthUserRequest;
import com.sufaltalukder.DTOs.RequestAuthLoginDTO;
import com.sufaltalukder.Mappers.AuthLoginAuditMapper;
import com.sufaltalukder.Mappers.AuthPermissionMapper;
import com.sufaltalukder.Mappers.AuthUserMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ActionLogModel.ActionLogMethod;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthLoginAuditModel;
import com.sufaltalukder.Models.AuthPermissionModel;
import com.sufaltalukder.Models.AuthPermissionModel.PermissionStatus;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.AuthUserModel.AuthUserActive;
import com.sufaltalukder.Models.AuthUserRefreshTokenModel;
import com.sufaltalukder.Repositories.AuthLoginAuditRepository;
import com.sufaltalukder.Repositories.AuthPermissionRepository;
import com.sufaltalukder.Repositories.AuthUserRefreshTokenRepository;
import com.sufaltalukder.Repositories.AuthUserRepository;
import com.sufaltalukder.Utils.AuthInfoUtil;
import com.sufaltalukder.Utils.AuthJwtUtil;
import com.sufaltalukder.feign.Services.ActionLogFeignService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ua_parser.Client;
import ua_parser.Parser;

@Service
@RequiredArgsConstructor
public class AuthUserMgmtServiceImpl implements AuthUserMgmtService {

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthLoginAuditRepository authLoginAuditRepository;

	@Autowired
	private AuthUserRefreshTokenRepository authUserRefreshTokenRepository;

	@Autowired
	private AuthPermissionRepository authPermissionRepository;

	@Autowired
	private ActionLogFeignService actionLogFeignService; // via feign client

	@Autowired
	private AuthJwtUtil authJwtUtil;

	private final String UPLOAD_DIR = "uploads";

	private final String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=]).{8,}$";

	private static final Logger log = LoggerFactory.getLogger(AuthUserMgmtServiceImpl.class);

	@Override
	public ApiResponse<AuthUserDTO> createAuthUser(long actionByUserId, @Valid AuthUserRequest authUserInfo,
			MultipartFile authUserImage) {

		AuthUserModel actionByUser = authUserRepository.findById(actionByUserId)
				.orElseThrow(() -> new RuntimeException("Action user not found"));

		String rawPassword = authUserInfo.getAuthUserPassword();

		if (rawPassword == null || rawPassword.isEmpty()) {
			return new ApiResponse<>("required", "Password is required.", null);
		}
		if (rawPassword.length() < 8) {
			return new ApiResponse<>("weak password", "Password minimum 8 characters needed.", null);
		}
		if (!Pattern.matches(passwordRegex, rawPassword)) {
			return new ApiResponse<>("invalid password", "Password does not meet the strength requirements.", null);
		}

		// Encode password
		String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes());

		authUserInfo.setAuthUserPassword(encodedPassword);
		authUserInfo.setActionByUserInfo(actionByUser);

		// Save user FIRST
		AuthUserModel savedUser = authUserRepository.save(AuthUserMapper.toEntity(authUserInfo));

		// Upload image using newly created authUserId
		if (authUserImage != null && !authUserImage.isEmpty()) {
			String imageName = storeAuthUserImage(savedUser.getAuthUserId(), authUserImage);
			savedUser.setAuthUserImage(imageName);
			authUserRepository.save(savedUser);
		}

		// Authorisation permission set by default
		AuthPermissionModel storingData = new AuthPermissionModel();

		storingData.setAuthUserInfo(savedUser);
		storingData.setActionByUserId(actionByUserId);
		storingData.setAddPermission(PermissionStatus.NO);
		storingData.setViewAllPermission(PermissionStatus.NO);
		storingData.setViewPermission(PermissionStatus.NO);
		storingData.setEditPermission(PermissionStatus.NO);
		storingData.setDeletePermission(PermissionStatus.NO);

		authPermissionRepository.save(storingData);

		// Action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(actionByUserId);
		actionLogData.setAuthUserId(savedUser.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Auth user created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Auth user created successfully.", AuthUserMapper.toDTO(savedUser));
	}

	@Override
	public ApiResponse<AuthTokenResponse> loginAuthUser(RequestAuthLoginDTO requestAuthLoginDTO,
			HttpServletRequest request) {

		AuthUserModel user = authUserRepository
				.findByAuthUserEmailAddress(requestAuthLoginDTO.getAuthUserEmailAddress());

		if (user == null) {
			saveAudit(null, request, "FAILED", "EMAIL_PASSWORD", "USER_NOT_FOUND");
			return new ApiResponse<>("unauthorized", "Invalid email or password.", null);
		}

		if (user.getAuthUserActive() == AuthUserActive.NO) {
			saveAudit(user, request, "FAILED", "EMAIL_PASSWORD", "USER_INACTIVE");
			return new ApiResponse<>("unauthorized", "Invalid email or password.", null);
		}

		if (user.getAuthUserActive() == AuthUserActive.ON_HOLD) {
			saveAudit(user, request, "FAILED", "EMAIL_PASSWORD", "USER_ON_HOLD");
			return new ApiResponse<>("unauthorized", "Currently, your're on hold. Get back soon.", null);
		}

		String encodedProvidedPassword = Base64.getEncoder()
				.encodeToString(requestAuthLoginDTO.getAuthUserPassword().getBytes());

		if (!encodedProvidedPassword.equals(user.getAuthUserPassword())) {
			saveAudit(null, request, "FAILED", "EMAIL_PASSWORD", "USER_NOT_MATCH");
			return new ApiResponse<>("not matched", "Provided email or password doesn't match.", null);
		}

		saveAudit(user, request, "SUCCESS", "EMAIL_PASSWORD", null);

		// Action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(user.getAuthUserId());
		actionLogData.setAuthUserId(user.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Login successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		// Generate JWT
		String accessToken = authJwtUtil.generateAccessToken(user.getAuthUserEmailAddress(), user.getAuthUserId());

		String refreshToken = authJwtUtil.generateRefreshToken(user.getAuthUserId());

		// save refresh token
		AuthUserRefreshTokenModel refreshTokenModel = new AuthUserRefreshTokenModel();
		refreshTokenModel.setAuthUserId(user.getAuthUserId());
		refreshTokenModel.setRefreshToken(refreshToken);
		refreshTokenModel.setExpiryDate(Instant.now().plus(7, ChronoUnit.DAYS));
		refreshTokenModel.setRevoked(false);

		authUserRefreshTokenRepository.save(refreshTokenModel);

		return new ApiResponse<>("success", "Login successfully.", new AuthTokenResponse(accessToken, refreshToken));

	}

	private void saveAudit(AuthUserModel user, HttpServletRequest request, String loginStatus, String authMethod,
			String failureReason) {
		try {
			String userAgent = request.getHeader("User-Agent");
			String ipAddress = AuthInfoUtil.getClientIp(request);

			// Parse User-Agent using uap-java
			Parser uaParser = new Parser();
			Client client = uaParser.parse(userAgent);

			AuthLoginAuditModel audit = new AuthLoginAuditModel();
			if (user != null)
				audit.setAuthUserInfo(user);

			audit.setIpAddress(ipAddress);
			audit.setUserAgent(userAgent);

			audit.setBrowser(client.userAgent.family);
			audit.setBrowserVersion(client.userAgent.major);
			audit.setOperatingSystem(client.os.family);
			audit.setOsVersion(client.os.major);
			audit.setDeviceType(
					client.device.family != null && !client.device.family.equals("Other") ? client.device.family
							: AuthInfoUtil.getDeviceType(userAgent));
			audit.setDeviceModel(client.device.family != null ? client.device.family : "UNKNOWN");

			audit.setPossibleIncognito(false);
			audit.setLoginStatus(loginStatus);
			audit.setAuthMethod(authMethod);
			audit.setFailureReason(failureReason);
			audit.setLoginTime(Instant.now());
			audit.setSessionId(request.getSession() != null ? request.getSession().getId() : null);
			audit.setReferrerUrl(request.getHeader("Referer"));

			authLoginAuditRepository.save(audit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String storeAuthUserImage(long authUserId, MultipartFile file) {
		try {
			Path uploadPath = Paths.get(UPLOAD_DIR);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			String fileName = authUserId + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

			Path filePath = uploadPath.resolve(fileName);
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			return fileName;

		} catch (IOException e) {
			throw new RuntimeException("Failed to store auth user image", e);
		}
	}

	@Override
	public ApiResponse<String> uploadImage(long authUserId, MultipartFile file) {

		Optional<AuthUserModel> isUserExits = authUserRepository.findById(authUserId);

		if (isUserExits.isPresent()) {
			AuthUserModel user = isUserExits.get();
			String existingImageFileName = user.getAuthUserImage();
			try {
				// Create the upload directory if it doesn't exist
				Path uploadPath = Paths.get(UPLOAD_DIR);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				// If there is an existing image, delete it
				if (existingImageFileName != null && !existingImageFileName.isEmpty()) {
					Path existingImagePath = uploadPath.resolve(existingImageFileName);
					try {
						Files.delete(existingImagePath);
					} catch (IOException e) {
						return new ApiResponse<>("error", "Failed to delete existing image: " + e.getMessage(), null);
					}
				}
				String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
				Path newFilePath = uploadPath.resolve(newFileName);
				Files.copy(file.getInputStream(), newFilePath);
				user.setAuthUserImage(newFileName);
				authUserRepository.save(user);

				// Push data inside actionLogFeignService
				ActionLogModel actionLogData = new ActionLogModel();
				actionLogData.setActionByAuthUserId(authUserId);
				actionLogData.setAuthUserId(authUserId);
				actionLogData.setActionLogMethod(ActionLogMethod.POST);
				actionLogData.setActionLogMessage("Auth image uploaded successfully.");
				actionLogFeignService.addActionLog(actionLogData);

				return new ApiResponse<>("success", "Auth image uploaded successfully.", newFileName);
			} catch (Exception e) {
				return new ApiResponse<>("error", "Failed to upload auth image: " + e.getMessage(), null);
			}
		} else {
			return new ApiResponse<>("not found", "Auth user ID not found.", null);
		}
	}

	@Override
	public ApiResponse<List<AuthUserDTO>> getAllAuthUsers() {

		List<AuthUserModel> fetchAllAuthUsers = authUserRepository.findAllAuthUsers();

		if (fetchAllAuthUsers.isEmpty()) {
			return new ApiResponse<>("not found", "No auth user(s) found.", null);
		}

		List<AuthUserDTO> dtos = fetchAllAuthUsers.stream().map(AuthUserMapper::toDTO).toList();

		return new ApiResponse<>("success", "All auth users fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<AuthUserDTO> getAuthUserDetails(long authUserId) {

		Optional<AuthUserModel> fetchAuthUser = authUserRepository.findById(authUserId);

		if (fetchAuthUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user details not found.", null);
		}

		return new ApiResponse<>("success", "Auth user details fetched successfully.",
				AuthUserMapper.toDTO(fetchAuthUser.get()));
	}

	@Override
	public ApiResponse<AuthResponseDTO> getAuthUser(long authUserId) {

		return authUserRepository.findById(authUserId).map(authUser -> {
			AuthResponseDTO dto = new AuthResponseDTO();
			dto.setAuthUserId(authUser.getAuthUserId());
			dto.setAuthUserEmailAddress(authUser.getAuthUserEmailAddress());
			dto.setAuthUserPhoneNumber(authUser.getAuthUserPhoneNumber());
			dto.setAuthUserName(authUser.getAuthUserName());
			dto.setAuthUserImage(authUser.getAuthUserImage());
			dto.setAuthUserActive(authUser.getAuthUserActive());
			dto.setAuthUserType(authUser.getAuthUserType());

			return new ApiResponse<>("success", "Auth user fetched successfully.", dto);

		}).orElseGet(() -> new ApiResponse<>("not found", "Auth user not found.", null));
	}

	@Override
	public ApiResponse<AuthUserDTO> updateAuthUser(long actionByUserId, long authUserId,
			@Valid AuthUserRequest authUserInfo, MultipartFile authUserImage) {

		Optional<AuthUserModel> optionalUser = authUserRepository.findById(authUserId);

		if (optionalUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user not found.", null);
		}

		AuthUserModel user = optionalUser.get();

		// Update fields
		user.setAuthUserName(authUserInfo.getAuthUserName());
		user.setAuthUserEmailAddress(authUserInfo.getAuthUserEmailAddress());
		user.setAuthUserPhoneNumber(authUserInfo.getAuthUserPhoneNumber());
		user.setAuthUserType(authUserInfo.getAuthUserType());
		user.setAuthUserActive(authUserInfo.getAuthUserActive());

		// Update password ONLY if provided
		if (authUserInfo.getAuthUserPassword() != null && !authUserInfo.getAuthUserPassword().isBlank()) {

			String encodedPassword = Base64.getEncoder().encodeToString(authUserInfo.getAuthUserPassword().getBytes());

			user.setAuthUserPassword(encodedPassword);
		}

		// Update image ONLY if new image provided
		if (authUserImage != null && !authUserImage.isEmpty()) {
			String imageName = storeAuthUserImage(authUserId, authUserImage);
			user.setAuthUserImage(imageName);
		}

		AuthUserModel updatedUser = authUserRepository.save(user);

		// Authorisation permission set by default
		AuthPermissionModel storingData = new AuthPermissionModel();

		storingData.setAuthUserInfo(updatedUser);
		storingData.setActionByUserId(actionByUserId);
		storingData.setAddPermission(PermissionStatus.NO);
		storingData.setViewAllPermission(PermissionStatus.NO);
		storingData.setViewPermission(PermissionStatus.NO);
		storingData.setEditPermission(PermissionStatus.NO);
		storingData.setDeletePermission(PermissionStatus.NO);

		authPermissionRepository.save(storingData);

		// Action log
		ActionLogModel log = new ActionLogModel();
		log.setActionByAuthUserId(actionByUserId);
		log.setAuthUserId(authUserId);
		log.setActionLogMethod(ActionLogMethod.PUT);
		log.setActionLogMessage("Auth user updated successfully.");
		actionLogFeignService.addActionLog(log);

		return new ApiResponse<>("success", "Auth user updated successfully.", AuthUserMapper.toDTO(updatedUser));
	}

	@Override
	public ApiResponse<AuthUserDTO> deleteAuthUser(long authUserId, long rqstAuthUserId) {

		Optional<AuthUserModel> fetchAuthUser = authUserRepository.findById(rqstAuthUserId);

		if (fetchAuthUser.isEmpty()) {
			return new ApiResponse<>("not found", "Auth user not found.", null);
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("Auth user deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		authUserRepository.deleteById(rqstAuthUserId);
		return new ApiResponse<>("success", "Auth user deleted successfully.", null);
	}

	@Override
	public ApiResponse<Void> deleteAllAuthUsers(long authUserId, List<Long> rqstAuthUserIds) {

		for (Long rqstAuthUserId : rqstAuthUserIds) {
			if (!authUserRepository.existsById(rqstAuthUserId)) {
				return new ApiResponse<>("not found", "Some auth users were not found.", null);
			}
		}

		// Push data inside actionLogFeignService
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.DELETE);
		actionLogData.setActionLogMessage("All specified auth users are deleted successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		authUserRepository.deleteAllById(rqstAuthUserIds);
		return new ApiResponse<>("success", "All specified auth users are deleted successfully.", null);
	}

	@Override
	public ApiResponse<List<AuthLoginAuditDTO>> getAuthUserLoginAudits() {

		List<AuthLoginAuditModel> auditLists = authLoginAuditRepository.findAllAuditDetails();

		if (auditLists.isEmpty()) {
			return new ApiResponse<>("not found", "No login audit(s) found.", null);
		}

		List<AuthLoginAuditDTO> dtos = auditLists.stream().map(AuthLoginAuditMapper::toDTO).toList();

		return new ApiResponse<>("success", "Login audit(s) list fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<AuthLoginAuditDTO> getAuthUserLoginAuditDetails(long authLoginAuditId) {

		AuthLoginAuditModel isAuditIdExists = authLoginAuditRepository.findLoginAuditDetailsById(authLoginAuditId);

		if (isAuditIdExists == null) {
			return new ApiResponse<>("not found", "No audit ID found.", null);
		}

		return new ApiResponse<>("success", "Login audit detail(s) fetched successfully.",
				AuthLoginAuditMapper.toDTO(isAuditIdExists));
	}

	@Override
	public ApiResponse<AuthUserDTO> createAccount(@Valid AuthUserRequest authUserInfo) {

		String rawPassword = authUserInfo.getAuthUserPassword();

		if (rawPassword == null || rawPassword.isEmpty()) {
			return new ApiResponse<>("required", "Password is required.", null);
		}
		if (rawPassword.length() < 8) {
			return new ApiResponse<>("weak password", "Password minimum 8 characters needed.", null);
		}
		if (!Pattern.matches(passwordRegex, rawPassword)) {
			return new ApiResponse<>("invalid password", "Password does not meet the strength requirements.", null);
		}

		// Encode password
		String encodedPassword = Base64.getEncoder().encodeToString(rawPassword.getBytes());

		authUserInfo.setAuthUserPassword(encodedPassword);
		authUserInfo.setActionByUserInfo(null);
		authUserInfo.setAuthUserActive(AuthUserActive.ON_HOLD);

		// Save user FIRST
		AuthUserModel savedUser = authUserRepository.save(AuthUserMapper.toEntity(authUserInfo));

		// Action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(0);
		actionLogData.setAuthUserId(savedUser.getAuthUserId());
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Account created successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Account created successfully.", AuthUserMapper.toDTO(savedUser));
	}

	@Override
	public ApiResponse<AuthPermissionDTO> grantAuthPermission(long authUserId,
			@Valid AuthPermissionRequest authPermissionRequest) {

		AuthUserModel authUser = authUserRepository.findById(authUserId)
				.orElseThrow(() -> new RuntimeException("Auth user not found"));

		Optional<AuthPermissionModel> existingPermission = authPermissionRepository
				.findByAuthPermissionId(authPermissionRequest.getAuthPermissionId());

		AuthPermissionModel storingData;

		if (existingPermission.isPresent()) {
			storingData = existingPermission.get();
		} else {
			storingData = new AuthPermissionModel();
			storingData.setAuthUserInfo(authUser);
		}

		storingData.setActionByUserId(authUserId);
		storingData.setAddPermission(authPermissionRequest.getAddPermission());
		storingData.setViewAllPermission(authPermissionRequest.getViewAllPermission());
		storingData.setViewPermission(authPermissionRequest.getViewPermission());
		storingData.setEditPermission(authPermissionRequest.getEditPermission());
		storingData.setDeletePermission(authPermissionRequest.getDeletePermission());

		AuthPermissionModel savedData = authPermissionRepository.save(storingData);

		// Action log
		ActionLogModel actionLogData = new ActionLogModel();
		actionLogData.setActionByAuthUserId(authUserId);
		actionLogData.setAuthUserId(authUserId);
		actionLogData.setActionLogMethod(ActionLogMethod.POST);
		actionLogData.setActionLogMessage("Auth permission saved successfully.");
		actionLogFeignService.addActionLog(actionLogData);

		return new ApiResponse<>("success", "Auth permission saved successfully.",
				AuthPermissionMapper.toDTO(savedData));
	}

	@Override
	public ApiResponse<List<AuthPermissionDTO>> getAuthsAllPermissions() {

		List<AuthPermissionModel> fetchAuthsAllPermissions = authPermissionRepository.findAllPermissions();

		if (fetchAuthsAllPermissions.isEmpty()) {
			return new ApiResponse<>("not found", "Auths permission(s) not found.", null);
		}

		List<AuthPermissionDTO> dtos = fetchAuthsAllPermissions.stream().map(AuthPermissionMapper::toDTO).toList();

		return new ApiResponse<>("success", "Auths permission(s) fetched successfully.", dtos);
	}
}