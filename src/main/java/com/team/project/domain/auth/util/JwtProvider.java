package com.team.project.domain.auth.util;

import java.nio.charset.StandardCharsets;
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
	//JWT 시크릿 키.
	@Value("${jwt.secret}")
	private String secret;
	//토큰 만료시간(밀리초).

	@Getter
	@Value("${jwt.expiry-millis}")
	private long expiryMillis;

	//토큰 생성 후 리턴

	public String generateToken(Authentication authentication) throws EntityNotFoundException {
		String username = authentication.getName();
		return this.generateTokenBy(username);
	}

	//입력받은 토큰에서 {@link Authentication}의 {@code username}을 리턴.

	public String getUsername(String token) {
		Claims claims = this.getClaims(token);
		return claims.getSubject();
	}

	//토큰이 유효한지 확인.

	public boolean validToken(String token) throws JwtException {
		try {
			return !this.tokenExpired(token);
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		}

		return false;
	}

	//이메일 주소를 이용해 토큰을 생성한 후 리턴
	private String generateTokenBy(String email) throws EntityNotFoundException {
		User user = this.userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("해당 email에 맞는 값이 존재하지 않습니다."));
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + this.expiryMillis);

		return Jwts.builder()
			.subject(email)
			.issuedAt(currentDate)
			.expiration(expireDate)
			.claim("role", user.getRole())
			.signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
			.compact();
	}

	//JWT의 claim 부분을 추출.
	private Claims getClaims(String token) {
		if (!StringUtils.hasText(token)) {
			throw new MalformedJwtException("토큰이 비어 있습니다.");
		}

		return Jwts.parser()
			.verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	//입력받은 토큰의 만료 여부.

	private boolean tokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// 받은 토큰의 만료일을 리턴
	private Date getExpirationDateFromToken(String token) {
		return this.resolveClaims(token, Claims::getExpiration);
	}

	//토큰에 입력 받은 로직을 적용하고 그 결과를 리턴.

	private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.getClaims(token);
		return claimsResolver.apply(claims);
	}
}