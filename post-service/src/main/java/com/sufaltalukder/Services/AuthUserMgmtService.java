package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.AuthUserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.AuthTokenResponse;
import com.sufaltalukder.Models.AuthUserModel;

public interface AuthUserMgmtService {

	ApiResponse<AuthTokenResponse> loginAuthUser(String authUserEmailAddress, String authUserPassword);

	ApiResponse<AuthUserDTO> createAuthUser(AuthUserModel authUserInfo);

	ApiResponse<List<AuthUserDTO>> getAllAuthUsers();

	ApiResponse<AuthUserDTO> getAuthUser(long authUserId);

	ApiResponse<AuthUserDTO> updateAuthUser(AuthUserModel authUserInfo);

	ApiResponse<AuthUserDTO> deleteAuthUser(long authUserId);

	ApiResponse<Void> deleteAllAuthUsers(List<Long> authUserIds);

	ApiResponse<String> uploadImage(long authUserId, MultipartFile file);

}