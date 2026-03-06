package com.team.project.domain.pg_provider.exception;

import com.team.project.global.common.exception.BaseException;

public class DuplicatePgProviderCodeException extends BaseException {

	public DuplicatePgProviderCodeException() {
		super("DUPLICATE_CODE");
	}

}
