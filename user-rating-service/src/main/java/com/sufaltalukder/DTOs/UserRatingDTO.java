package com.sufaltalukder.DTOs;

import java.time.ZonedDateTime;

import com.sufaltalukder.Models.UserModel;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRatingDTO {

	private long userRatingId;
	private long authUserId;
	private long userId;
	private UserModel userInfo;
	private long productId;
	private int userRating;
	private String userComment;
	private ZonedDateTime userRatingCreatedAt;
	private ZonedDateTime userRatingUpdatedAt;
}
