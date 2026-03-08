package com.team.project.global.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.team.project.domain.auth.util.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtProvider jwtProvider;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		// 1. 검증
		authenticate(request);

		// 2. 필터 통과
		filterChain.doFilter(request, response);
	}

	private void authenticate(HttpServletRequest request) {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return;
		}

		// 1. 요청에서 토큰 추출
		String token = getTokenFromRequest(request);

		// 2. 토큰 검증
		if (!jwtProvider.validToken(token)) {
			return;
		}

		// 3. 로그인 아이디 뽑기
		String loginId = jwtProvider.getLoginId(token);

		// 4. 로그인 아이디로 UserDetails 뽑기
		UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

		// 5. 시큐리티 컨텍스트에 저장
		setAuthentication(request, userDetails);
	}

	/**
	 *  요청에서 토큰 뽑기
	 */
	private String getTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX.length());
		}

		return null;
	}

	/**
	 *  시큐리티 컨텍스트에 저장
	 */
	private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities()
			);

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
