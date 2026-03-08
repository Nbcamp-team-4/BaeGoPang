package com.team.project.domain.auth.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.team.project.domain.auth.api.request.KakaoLogin;
import com.team.project.domain.auth.api.request.KakaoToken;
import com.team.project.domain.auth.api.response.JwtAuthResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OAuthService {
	public KakaoToken getKakaoAccessToken(String code) {
		return null;
	}

	public JwtAuthResponse login(KakaoLogin loginDto) {
		return null;
	}

	public HashMap<String, Object> getKakaoUserInfo(String accessToken) {
		return null;
	}
}
