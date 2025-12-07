package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppBannerDTO {

	private long appBannerId;
	private long authUserId;
	private String appBannerImage;
	private ZonedDateTime appBannerCreatedAt;
	private ZonedDateTime appBannerUpdatedAt;
}
