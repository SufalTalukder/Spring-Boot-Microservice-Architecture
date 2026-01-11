package com.sufaltalukder.DTOs;

import java.time.Instant;

import com.sufaltalukder.Models.AuthUserModel;
import com.sufaltalukder.Models.UserModel;
import com.sufaltalukder.Models.SupportModel.SupportStatus;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportDTO {

	private long supportId;
	private AuthUserModel authUserInfo;
	private UserModel userInfo;
	private String supportText;
	private SupportStatus supportStatus;
	private Instant supportCreatedAt;
	private Instant supportUpdatedAt;

}
