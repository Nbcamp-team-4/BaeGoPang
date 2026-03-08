package com.team.project.domain.auth.api.request;

import lombok.Getter;

@Getter
public class KakaoLogin {

	private String nickname;

	private Long id;
	private String tokenAuthScheme;

	private String accessToken;

	public KakaoLogin(String nickname, Long id) {
		this.nickname = nickname;
		this.id = id;
	}
}

