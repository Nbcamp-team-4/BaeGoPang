package com.team.project.domain.user.api.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginUserRequest {

	@NotNull
	private UUID userId;

	@NotNull
	private String loginId;

	@NotNull
	private String password;

}
