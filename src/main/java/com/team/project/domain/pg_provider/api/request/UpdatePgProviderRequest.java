package com.team.project.domain.pg_provider.api.request;

import com.team.project.domain.pg_provider.model.vo.PgProviderStatus;

import lombok.Data;

@Data
public class UpdatePgProviderRequest {
	private String code;
	private String name;
	private PgProviderStatus status;
}
