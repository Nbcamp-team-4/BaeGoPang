package com.team.project.domain.auth.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
}
