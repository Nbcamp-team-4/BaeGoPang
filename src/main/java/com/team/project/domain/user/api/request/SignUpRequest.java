package com.team.project.domain.user.api.request;

import com.team.project.domain.user.entity.RoleType;
import com.team.project.domain.user.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

	@NotBlank(message = "loginId는 빈 값이 허용되지 않습니다.")
	@Pattern(
			regexp = "^[a-z0-9]{4,10}$",
			message = "loginId는 4~10자의 영문 소문자와 숫자만 가능합니다.")
	private final String loginId;

	@Pattern(
			regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,15}$",
			message = "password는 8~15자의 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
	@NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
	private final String password;

	@NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
	@Email(message = "올바른 email 형식이 아닙니다.")
	private final String email;

	@NotBlank(message = "name은 빈 값이 허용되지 않습니다.")
	private final String name;

	@NotBlank(message = "phone은 빈 값이 허용되지 않습니다.")
	private final String phone;

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
