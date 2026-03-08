package com.team.project.domain.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

	private final UserRepository userRepository;

	@Value("${jwt.secret}")
	private String secret;

	@Getter
	@Value("${jwt.expiry-millis}")
	private long expiryMillis;

	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		return generateTokenBy(username);
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		return claims.getSubject();
	}

	public boolean validToken(String token) {
		try {
			getClaims(token);
			return !tokenExpired(token);
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

	private String generateTokenBy(String email) {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("해당 email에 맞는 값이 존재하지 않습니다."));

		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + expiryMillis);

		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(currentDate)
			.setExpiration(expireDate)
			.claim("roles", user.getUserRoles())
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

	private boolean tokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Date getExpirationDateFromToken(String token) {
		return resolveClaims(token, Claims::getExpiration);
	}

	private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getClaims(token);
		return claimsResolver.apply(claims);
	}

	private Key getSigningKey() {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}