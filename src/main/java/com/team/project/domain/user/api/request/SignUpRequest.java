package com.team.project.domain.user.api.request;

import com.team.project.domain.user.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

	@NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
	@Email(message = "올바른 email 형식이 아닙니다.")
	@Pattern(regexp = "^[a-z0-9]{4,10}$")
	private final String loginId;

	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,15}$")
	@NotBlank(message = "password는 빈 값이 허용되지 않습니다.")
	private final String password;

	@NotBlank(message = "email은 빈 값이 허용되지 않습니다.")
	@Email(message = "올바른 email 형식이 아닙니다.")
	private final String email;

	@NotBlank(message = "name은 빈 값이 허용되지 않습니다.")
	private final String name;

	@NotBlank(message = "phone은 빈 값이 허용되지 않습니다.")
	private final String phone;

	private final UserStatus status;

	public SignUpRequest(String loginId, String password, String email, String name, String phone, UserStatus status) {
		this.loginId = loginId;
		this.password = password;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.status = status;
	}

}
