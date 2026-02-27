package com._team._project.domain.pg_provider.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePgProviderRequest {
	@NotBlank
	@Size(max = 50)
	private String code;
	@NotBlank
	@Size(max = 50)
	private String name;
}
