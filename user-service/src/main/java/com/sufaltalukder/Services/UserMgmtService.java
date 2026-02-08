package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.DTOs.UserRequest;
import com.sufaltalukder.Models.ApiResponse;

import jakarta.validation.Valid;

public interface UserMgmtService {

	ApiResponse<UserDTO> createUser(long authUserId, @Valid UserRequest userInfo, MultipartFile userImage);

	ApiResponse<UserDTO> fetchUserDetails(long authUserId, long userId);

	ApiResponse<List<UserDTO>> fetchUsersList();

	ApiResponse<String> uploadUserImage(long authUserId, long userId, MultipartFile file);

	ApiResponse<UserDTO> updateUserDetail(long authUserId, long userId, @Valid UserRequest userInfo,
			MultipartFile userImage);

	ApiResponse<UserDTO> deleteUser(long authUserId, long userId);

}
