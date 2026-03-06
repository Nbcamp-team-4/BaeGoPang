package com.team.project.domain.pg_provider.exception;

import com.team.project.global.common.exception.BaseException;

public class AlreadyDeactivatedPgProviderException extends BaseException {

	public AlreadyDeactivatedPgProviderException() {
		super("ALREADY_DEACTIVE_PG_PROVIDER");
	}
}
