package com.sufaltalukder.Services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Mappers.ActionLogMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Repositories.ActionLogRepository;

@Service
public class ActionLogMgmtServiceImpl implements ActionLogMgmtService {

	@Autowired
	private ActionLogRepository actionLogRepository;

	@Override
	public ApiResponse<ActionLogDTO> addActionLog(ActionLogModel actionLogModel) {

		ActionLogModel saved = actionLogRepository.save(actionLogModel);

		return new ApiResponse<>("success", "Action log added successfully.", ActionLogMapper.toDTO(saved));
	}

	@Override
	public ApiResponse<List<ActionLogDTO>> getAuthActionLogs(long authUserId) {

		List<ActionLogModel> actionLogs = actionLogRepository.findAllActionLogs(authUserId);

		if (actionLogs.isEmpty()) {
			return new ApiResponse<>("not found", "No action log(s) found.", null);
		}

		List<ActionLogDTO> dtos = actionLogs.stream().map(ActionLogMapper::toDTO).toList();

		return new ApiResponse<>("success", "All action log(s) fetched successfully.", dtos);
	}

	@Override
	public ApiResponse<List<ActionLogDTO>> getUserActionLogsByAuth(long rqstAuthUserId) {

		List<ActionLogModel> authUserActionLogs = actionLogRepository.findAllUserActionLogs(rqstAuthUserId);

		if (authUserActionLogs.isEmpty()) {
			return new ApiResponse<>("not found", "No auth user action log(s) found.", null);
		}

		List<ActionLogDTO> dtos = authUserActionLogs.stream().map(ActionLogMapper::toDTO).toList();

		return new ApiResponse<>("success", "Auth user all action log(s) fetched successfully.", dtos);
	}
}