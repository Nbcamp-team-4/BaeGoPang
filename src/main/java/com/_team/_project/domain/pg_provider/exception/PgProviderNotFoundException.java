package com._team._project.domain.pg_provider.exception;

import com._team._project.global.common.exception.BaseException;

public class PgProviderNotFoundException extends BaseException {

	public PgProviderNotFoundException() {
		super("NOT_FOUND_PG_PROVIDER");
	}
}
