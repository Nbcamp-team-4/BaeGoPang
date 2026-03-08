package com.team.project.domain.auth.api.request;

import lombok.Getter;

@Getter
public class OAuthDto {

	private String grantType;

	private String clientId;

	private String redirectUri;

	private String code;

	public OAuthDto(String grantType, String clientId, String redirectUri, String code) {
		this.grantType = grantType;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
		this.code = code;
	}
}

