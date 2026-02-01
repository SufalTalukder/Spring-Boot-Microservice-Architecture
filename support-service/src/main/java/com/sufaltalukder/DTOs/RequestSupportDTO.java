package com.sufaltalukder.DTOs;

import com.sufaltalukder.Models.SupportModel.SupportStatus;

import lombok.Data;

@Data
public class RequestSupportDTO {

	private String supportText;
	private SupportStatus supportStatus;

}
