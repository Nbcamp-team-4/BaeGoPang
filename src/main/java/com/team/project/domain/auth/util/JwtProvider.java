package com.team.project.domain.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.exception.UserNotFoundException;
import com.team.project.domain.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

	private final UserRepository userRepository;

	@Value("${jwt.secret.key}")
	private String secret;

	@Getter
	@Value("${jwt.expiry-millis}")
	private long expiryMillis;

	public String generateToken(Authentication authentication) {
		String loginId = authentication.getName();
		return generateTokenBy(loginId);
	}

	public String getLoginId(String token) {
		return getClaims(token).getSubject();
	}

	public boolean validToken(String token) {
		try {
			getClaims(token);
			return true;
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (JwtException e) {
			log.error("JWT claims string is empty or invalid: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT token compact of handler are invalid: {}", e.getMessage());
		}
		return false;
	}

	private String generateTokenBy(String loginId) {
		User user = userRepository.findByLoginId(loginId)
			.orElseThrow(UserNotFoundException::new);

		Date now = new Date();
		Date expiry = new Date(now.getTime() + expiryMillis);

		List<String> roles = user.getUserRoles().stream()
			.map(userRole -> userRole.getRole().getRole().name())
			.toList();

		return Jwts.builder()
			.setSubject(loginId)
			.setIssuedAt(now)
			.setExpiration(expiry)
			.claim("roles", roles)
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	private Claims getClaims(String token) {
		if (!StringUtils.hasText(token)) {
			throw new MalformedJwtException("토큰이 비어 있습니다.");
		}

		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		if (keyBytes.length < 32) {
			throw new IllegalArgumentException("JWT secret key는 최소 32바이트 이상이어야 합니다.");
		}
		return Keys.hmacShaKeyFor(keyBytes);
	}
}