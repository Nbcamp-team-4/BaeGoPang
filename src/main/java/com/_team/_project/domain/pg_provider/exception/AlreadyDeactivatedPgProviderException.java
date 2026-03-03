package com._team._project.domain.pg_provider.exception;

import com._team._project.global.common.exception.BaseException;

public class AlreadyDeactivatedPgProviderException extends BaseException {

	public AlreadyDeactivatedPgProviderException() {
		super("ALREADY_DEACTIVE_PG_PROVIDER");
	}
}
