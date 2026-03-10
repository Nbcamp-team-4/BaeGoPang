package com.team.project.global.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.team.project.domain.auth.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService customUserDetailsService;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		// 1. OPTIONS 요청은 필터를 타지 않게 함 (CORS 해결의 핵심)
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			return true;
		}
		String uri = request.getRequestURI();

		return uri.equals("/")
				|| uri.equals("/error")
				|| uri.equals("/favicon.ico")
				|| uri.equals("/api/auth/login")
				|| uri.equals("/api/auth/signup")
				|| uri.equals("/api/auth/reissue")
				|| uri.startsWith("/swagger-ui/")
				|| uri.startsWith("/v3/api-docs/");
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {

		String token = jwtTokenProvider.resolveBearerToken(request);

		if (token != null) {
			try {
				if (jwtTokenProvider.isAccessToken(token)) {
					String loginId = jwtTokenProvider.getLoginId(token);
					UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginId);

					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
						);

					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (JWTVerificationException e) {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}
}