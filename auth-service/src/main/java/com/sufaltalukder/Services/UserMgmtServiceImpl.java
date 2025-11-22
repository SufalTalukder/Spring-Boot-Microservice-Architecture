package com.sufaltalukder.Services;

import java.time.ZonedDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.UserDTO;
import com.sufaltalukder.Mappers.UserMapper;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Repositories.UserRepository;

@Service
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public ApiResponse<UserDTO> createUser(UserModel userInfo) {
		List<UserModel> isPhoneNumberExist = userRepository.findByPhoneNumber(userInfo.getPhoneNumber());

		UserModel targetUser = null;

		// Pick the first matching record if duplicates exist (temporary safeguard)
		if (isPhoneNumberExist != null && !isPhoneNumberExist.isEmpty()) {
			targetUser = isPhoneNumberExist.get(0);
		}
		if (targetUser != null) {
			return new ApiResponse<>("exist", "Phone number is already taken by another user!", null);
		}

		UserModel newSavedData = userRepository.save(userInfo);
		return new ApiResponse<>("success", "User created successfully.", UserMapper.toDTO(newSavedData));
	}

	@Override
	public ApiResponse<UserDTO> getByUser(long userId) {
		Optional<UserModel> isUserIdExist = userRepository.findById(userId);

		if (isUserIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}

		return new ApiResponse<>("success", "User fetched successfully.", UserMapper.toDTO(isUserIdExist.get()));
	}

	@Override
	public PaginationApiResponse<List<UserDTO>> getAllUsers(int pageNo, int pageSize) {
		Page<UserModel> users = userRepository.findAll(PageRequest.of(pageNo - 1, pageSize));

		if (users.isEmpty()) {
			return new PaginationApiResponse<>("not found", "User not found.", null, 0, 0, 0);
		}

		List<UserDTO> dtos = users.stream().map(UserMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Users list fetched successfully.", dtos, users.getNumber() + 1,
				users.getSize(), users.getTotalElements());
	}

	@Override
	public ApiResponse<UserDTO> updateUser(long userId, UserModel userInfo) {
		Optional<UserModel> isUserIdExist = userRepository.findById(userId);

		if (isUserIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		} else {
			UserModel updateData = isUserIdExist.get();
			List<UserModel> findUserByPhoneNumber = userRepository.findByPhoneNumber(updateData.getPhoneNumber());

			UserModel targetUser = null;

			// Pick the first matching record if duplicates exist (temporary safeguard)
			if (findUserByPhoneNumber != null && !findUserByPhoneNumber.isEmpty()) {
				targetUser = findUserByPhoneNumber.get(0);
			}

			if (targetUser != null) {
				updateData.setFullName(userInfo.getFullName());
				updateData.setPhoneNumber(userInfo.getPhoneNumber());
				updateData.setEmailAddress(userInfo.getEmailAddress());
				updateData.setDob(userInfo.getDob());
				updateData.setUserActive(userInfo.getUserActive());
				updateData.setUserUpdatedAt(ZonedDateTime.now());

				UserModel newUpdatedData = userRepository.save(updateData);
				return new ApiResponse<>("success", "User updated successfully.", UserMapper.toDTO(newUpdatedData));
			} else {
				return new ApiResponse<>("exist",
						"Phone number is already taken by another user or phone number not exist!", null);
			}
		}
	}

	@Override
	public ApiResponse<UserDTO> deleteUser(long userId) {
		Optional<UserModel> isUserIdExist = userRepository.findById(userId);

		if (isUserIdExist.isEmpty()) {
			return new ApiResponse<>("not found", "User not found.", null);
		}
		userRepository.deleteById(userId);

		return new ApiResponse<>("success", "User deleted successfully.", null);
	}
}
