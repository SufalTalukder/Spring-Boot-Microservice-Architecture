package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserModel;

public interface UserMgmtService {

	ApiResponse<UserDTO> createUser(UserModel userInfo);

	ApiResponse<UserDTO> getByUser(long userId);

	PaginationApiResponse<List<UserDTO>> getAllUsers(int pageNo, int pageSize);

	ApiResponse<UserDTO> updateUser(long userId, UserModel userInfo);

	ApiResponse<UserDTO> deleteUser(long userId);

}
