package com.team.project.domain.auth.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserRequest {

	@NotNull
	private String loginId;

	@NotNull
	private String password;

}
