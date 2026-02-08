package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.DTOs.ActionLogRequest;
import com.sufaltalukder.Models.ApiResponse;

import jakarta.validation.Valid;

public interface ActionLogMgmtService {

	ApiResponse<ActionLogDTO> addActionLog(@Valid ActionLogRequest actionLogRequest);

	ApiResponse<List<ActionLogDTO>> getAuthActionLogs(long authUserId);

	ApiResponse<List<ActionLogDTO>> getUserActionLogsByAuth(long rqstAuthUserId);

}
