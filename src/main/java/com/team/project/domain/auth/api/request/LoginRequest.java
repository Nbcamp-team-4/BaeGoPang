package com.team.project.domain.auth.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
	@Schema(description = "로그인 ID", example = "testId12")
	private String loginId;
	@Schema(description = "비밀번호", example = "Test1234!")
	private String password;
}