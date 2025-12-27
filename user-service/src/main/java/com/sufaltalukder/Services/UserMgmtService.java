package com.sufaltalukder.Services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.UserModel;

public interface UserMgmtService {

	ApiResponse<UserDTO> createUser(long authUserId, UserModel userInfo);

	ApiResponse<UserDTO> fetchUserDetails(long authUserId, long userId);

	ApiResponse<List<UserDTO>> fetchUsersList();

	ApiResponse<String> uploadUserImage(long authUserId, long userId, MultipartFile file);

	ApiResponse<UserDTO> updateUserDetail(long authUserId, long userId, UserModel userInfo);

	ApiResponse<UserDTO> deleteUser(long authUserId, long userId);

}
