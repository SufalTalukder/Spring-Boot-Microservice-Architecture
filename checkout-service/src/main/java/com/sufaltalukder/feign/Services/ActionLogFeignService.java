package com.sufaltalukder.feign.Services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sufaltalukder.DTOs.ActionLogDTO;
import com.sufaltalukder.Models.ActionLogModel;
import com.sufaltalukder.Models.ApiResponse;

@FeignClient(name = "ACTION-LOG-SERVICE")
public interface ActionLogFeignService {

	@PostMapping("/api/v1/elastic/auth/add-action-log")
	ApiResponse<ActionLogDTO> addActionLog(@RequestBody ActionLogModel actionLogModel);
}
