package com.team.project.domain.auth.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import com.team.project.domain.auth.util.JwtUtil;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AuthFilter")
//@Component
@Order(2)
public class AuthFilter implements Filter {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String url = httpServletRequest.getRequestURI();

		if (StringUtils.hasText(url) &&
			(url.startsWith("/api/user") || url.startsWith("/css") || url.startsWith("/js"))
		) {
			// 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
			chain.doFilter(request, response); // 다음 Filter 로 이동
		} else {
			// 나머지 API 요청은 인증 처리 진행
			// 토큰 확인
			String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

			if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
				// JWT 토큰 substring
				String token = jwtUtil.substringToken(tokenValue);

				// 토큰 검증
				if (!jwtUtil.validateToken(token)) {
					throw new IllegalArgumentException("Token Error");
				}

				// 토큰에서 사용자 정보 가져오기
				Claims info = jwtUtil.getUserInfoFromToken(token);

				User user = userRepository.findByLoginId(info.getSubject()).orElseThrow(() ->
					new NullPointerException("Not Found User")
				);

				request.setAttribute("user", user);
				chain.doFilter(request, response); // 다음 Filter 로 이동
			} else {
				throw new IllegalArgumentException("Not Found Token");
			}
		}
	}

}
