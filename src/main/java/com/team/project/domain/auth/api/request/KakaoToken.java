package com.team.project.domain.auth.api.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class KakaoToken {
	private String access_token;
	private String token_type;
	private String refresh_token;
	private String id_token;
	private int expires_in;
	private int refresh_token_expires_in;
	private String scope;
}
