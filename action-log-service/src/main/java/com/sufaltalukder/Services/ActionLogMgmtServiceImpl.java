package com.sufaltalukder.Services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Mappers.ActionLogMapper;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;
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
	public PaginationApiResponse<List<ActionLogDTO>> getAllActionLogs(int pageNo, int pageSize) {

		Page<ActionLogModel> actionLogs = actionLogRepository.findAllActionLogs(PageRequest.of(pageNo - 1, pageSize));

		if (actionLogs.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No action log(s) found.", null, 0, 0, 0);
		}

		List<ActionLogDTO> dtos = actionLogs.map(ActionLogMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "All action log(s) fetched successfully.", dtos,
				actionLogs.getNumber() + 1, actionLogs.getSize(), actionLogs.getTotalElements());
	}

	@Override
	public PaginationApiResponse<List<ActionLogDTO>> getAllAuthUserActionLogs(long authUserId, int pageNo,
			int pageSize) {

		Page<ActionLogModel> authUserActionLogs = actionLogRepository.findAllUserActionLogs(authUserId,
				PageRequest.of(pageNo - 1, pageSize));

		if (authUserActionLogs.isEmpty()) {
			return new PaginationApiResponse<>("not found", "No auth user action log(s) found.", null, 0, 0, 0);
		}

		List<ActionLogDTO> dtos = authUserActionLogs.map(ActionLogMapper::toDTO).toList();

		return new PaginationApiResponse<>("success", "Auth user all action log(s) fetched successfully.", dtos,
				authUserActionLogs.getNumber() + 1, authUserActionLogs.getSize(),
				authUserActionLogs.getTotalElements());
	}
}