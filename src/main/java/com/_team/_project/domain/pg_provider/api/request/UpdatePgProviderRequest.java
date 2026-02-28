package com._team._project.domain.pg_provider.api.request;

import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;

import lombok.Data;

@Data
public class UpdatePgProviderRequest {
	private String code;
	private String name;
	private PgProviderStatus status;
}
