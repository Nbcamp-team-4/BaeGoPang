package com.team.project.domain.auth.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {
	@Schema(description = "로그인 ID", example = "testId12")
	@NotBlank(message = "loginId는 빈 값이 허용되지 않습니다.")
	private String loginId;
	@Schema(description = "비밀번호", example = "Test1234!")
	@NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
	private String password;

	public LoginRequest(String loginId, String password) {
		this.loginId = loginId;
		this.password = password;
	}
}