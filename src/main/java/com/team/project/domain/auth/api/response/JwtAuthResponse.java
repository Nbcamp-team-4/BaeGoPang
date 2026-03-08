package com.team.project.domain.auth.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtAuthResponse {

	//access token 인증 방식.
	private String tokenAuthScheme;

	// access token.

	private String accessToken;

	public JwtAuthResponse(String tokenAuthScheme, String accessToken) {
		this.tokenAuthScheme = tokenAuthScheme;
		this.accessToken = accessToken;
	}

	public String getToken() {
		return this.accessToken; // getToken() 메서드 추가
	}
}
