package com.team.project.domain.auth.api.request;

import com.team.project.domain.user.entity.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

	@Schema(description = "로그인 ID", example = "test1234")
	@NotBlank(message = "loginId는 빈 값이 허용되지 않습니다.")
	@Pattern(
			regexp = "^[a-z0-9]{4,10}$",
			message = "loginId는 4~10자의 영문 소문자와 숫자만 가능합니다.")
	private final String loginId;

	@Schema(description = "비밀번호", example = "Test1234!")
	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,15}$",
			message = "password는 8~15자의 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
	@NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
	private final String password;

	@Schema(description = "email", example = "test1234@naver.com")
	@NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
	@Email(message = "올바른 email 형식이 아닙니다.")
	private final String email;

	@Schema(description = "이름", example = "홍길동")
	@NotBlank(message = "name은 빈 값이 허용되지 않습니다.")
	private final String name;

	@Schema(description = "번호", example = "010-1111-1111")
	@NotBlank(message = "phone은 빈 값이 허용되지 않습니다.")
	private final String phone;

	@Schema(description = "권한", example = "ROLE_ADMIN")
	@NotBlank(message = "role 빈 값이 허용되지 않습니다.")
	private final RoleType role;

	public SignUpRequest(String loginId, String password, String email, String name, String phone, RoleType role) {
		this.loginId = loginId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.phone = phone;
        this.role = role;
    }

}
