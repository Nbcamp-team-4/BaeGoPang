package com._team._project.domain.pg_provider.exception;

import com._team._project.global.common.exception.BaseException;

public class DuplicatePgProviderCodeException extends BaseException {

	public DuplicatePgProviderCodeException() {
		super("DUPLICATE_CODE");
	}

}
