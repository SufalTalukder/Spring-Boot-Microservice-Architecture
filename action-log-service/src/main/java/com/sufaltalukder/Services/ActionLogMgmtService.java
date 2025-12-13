package com.sufaltalukder.Services;

import java.util.List;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;
import com.sufaltalukder.Models.PaginationApiResponse;

public interface ActionLogMgmtService {

	ApiResponse<ActionLogDTO> addActionLog(ActionLogModel actionLogModel);

	PaginationApiResponse<List<ActionLogDTO>> getAllActionLogs(int pageNo, int pageSize);

	PaginationApiResponse<List<ActionLogDTO>> getAllAuthUserActionLogs(long authUserId, int pageNo, int pageSize);

}
