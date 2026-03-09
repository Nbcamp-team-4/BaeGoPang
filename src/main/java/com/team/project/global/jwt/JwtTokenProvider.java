package com.team.project.global.jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.team.project.domain.auth.dto.CustomUserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.access-token-expiration-seconds}")
	private long accessTokenExpirationSeconds;

	@Value("${jwt.refresh-token-expiration-seconds}")
	private long refreshTokenExpirationSeconds;

	private static final String CLAIM_USER_ID = "userId";
	private static final String CLAIM_LOGIN_ID = "loginId";
	private static final String CLAIM_ROLES = "roles";
	private static final String CLAIM_TYPE = "type";

	public String createAccessToken(CustomUserPrincipal principal) {
		Instant now = Instant.now();

		return JWT.create()
			.withSubject(principal.getLoginId())
			.withClaim(CLAIM_USER_ID, principal.getUserId().toString())
			.withClaim(CLAIM_LOGIN_ID, principal.getLoginId())
			.withArrayClaim(
				CLAIM_ROLES,
				principal.getAuthorities().stream()
					.map(GrantedAuthority::getAuthority)
					.toArray(String[]::new)
			)
			.withClaim(CLAIM_TYPE, "ACCESS")
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(now.plusSeconds(accessTokenExpirationSeconds)))
			.sign(Algorithm.HMAC256(secret));
	}

	public String createRefreshToken(CustomUserPrincipal principal) {
		Instant now = Instant.now();

		return JWT.create()
			.withSubject(principal.getLoginId())
			.withClaim(CLAIM_USER_ID, principal.getUserId().toString())
			.withClaim(CLAIM_TYPE, "REFRESH")
			.withIssuedAt(Date.from(now))
			.withExpiresAt(Date.from(now.plusSeconds(refreshTokenExpirationSeconds)))
			.sign(Algorithm.HMAC256(secret));
	}

	public DecodedJWT verify(String token) {
		return JWT.require(Algorithm.HMAC256(secret))
			.build()
			.verify(token);
	}

	public boolean isAccessToken(String token) {
		return "ACCESS".equals(verify(token).getClaim(CLAIM_TYPE).asString());
	}

	public boolean isRefreshToken(String token) {
		return "REFRESH".equals(verify(token).getClaim(CLAIM_TYPE).asString());
	}

	public String getLoginId(String token) {
		return verify(token).getClaim(CLAIM_LOGIN_ID).asString();
	}

	public UUID getUserId(String token) {
		return UUID.fromString(verify(token).getClaim(CLAIM_USER_ID).asString());
	}

	public List<String> getRoles(String token) {
		String[] roles = verify(token).getClaim(CLAIM_ROLES).asArray(String.class);
		return roles == null ? List.of() : Arrays.asList(roles);
	}

	public String resolveBearerToken(HttpServletRequest request) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization == null || !authorization.startsWith("Bearer ")) {
			return null;
		}
		return authorization.substring(7);
	}
}