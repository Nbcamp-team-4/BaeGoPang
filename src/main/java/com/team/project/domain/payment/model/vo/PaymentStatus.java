package com.team.project.domain.payment.model.vo;

public enum PaymentStatus {
	READY, PAID, CANCELED, CANCEL_FAILED;

	// READY -> PAID -> CANCELED -> CANCEL_FAILED

	public boolean isReady() {
		return this == READY;
	}

	public boolean isInProgress() {
		return this == READY;
	}
}
