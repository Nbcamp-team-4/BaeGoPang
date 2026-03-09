package com.team.project.domain.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team.project.domain.auth.api.request.LoginRequest;
import com.team.project.domain.auth.api.request.SignUpRequest;
import com.team.project.domain.auth.api.response.LoginResponse;
import com.team.project.domain.auth.dto.CurrentUser;
import com.team.project.domain.auth.dto.UserDto;
import com.team.project.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/reissue")
	public ResponseEntity<LoginResponse> reissue(@RequestHeader("Refresh-Token") String refreshToken) {
		return ResponseEntity.ok(authService.reissue(refreshToken));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CurrentUser UserDto userDto) {
		authService.logout(userDto.getId());
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/signup")
	public ResponseEntity<Void> signUp(@RequestBody SignUpRequest request) {
		authService.signUp(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}