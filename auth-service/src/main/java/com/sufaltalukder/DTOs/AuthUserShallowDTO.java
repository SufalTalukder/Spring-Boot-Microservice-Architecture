package com.sufaltalukder.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserShallowDTO {

	private long authUserId;
	private String authUserName;

}
