package com.team.project.domain.pg_provider.api.request;

import com.team.project.domain.pg_provider.model.vo.PgProviderStatus;
import com.team.project.global.common.dto.BasePageRequest;

import lombok.Data;

@Data
public class GetPgProvidersRequest extends BasePageRequest {
	private String code;
	private String name;
	private PgProviderStatus status;

	public GetPgProvidersRequest(Integer page, Integer size, String code, String name, PgProviderStatus status) {
		super(page, size);
		this.code = code;
		this.name = name;
		this.status = status;
	}
}
