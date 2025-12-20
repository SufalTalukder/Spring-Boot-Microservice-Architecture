package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;

public interface ActionLogMgmtService {

	ApiResponse<ActionLogDTO> addActionLog(ActionLogModel actionLogModel);

	ApiResponse<List<ActionLogDTO>> getAuthActionLogs(long authUserId);

	ApiResponse<List<ActionLogDTO>> getUserActionLogsByAuth(long rqstAuthUserId);

}
