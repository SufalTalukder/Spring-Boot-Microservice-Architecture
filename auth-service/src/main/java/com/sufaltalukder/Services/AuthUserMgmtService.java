package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AuthLoginAuditDTO;
import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.DTOs.AuthUserRequest;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthUserMgmtService {

	ApiResponse<AuthTokenResponse> loginAuthUser(String authUserEmailAddress, String authUserPassword,
			HttpServletRequest request);

	ApiResponse<AuthUserDTO> createAuthUser(long authUserId, AuthUserRequest authUserInfo, MultipartFile authUserImage);

	ApiResponse<List<AuthUserDTO>> getAllAuthUsers();

	ApiResponse<AuthUserDTO> getAuthUser(long authUserId);

	ApiResponse<AuthUserDTO> getAuthUserDetails(long authUserId);

	ApiResponse<List<AuthLoginAuditDTO>> getAuthUserLoginAuditDetails();

	ApiResponse<AuthUserDTO> updateAuthUser(long actionByUserId, long authUserId, AuthUserRequest authUserInfo,
			MultipartFile authUserImage);

	ApiResponse<AuthUserDTO> deleteAuthUser(long authUserId, long rqstAuthUserId);

	ApiResponse<Void> deleteAllAuthUsers(long authUserId, List<Long> rqstAuthUserIds);

	ApiResponse<String> uploadImage(long authUserId, MultipartFile file);

}