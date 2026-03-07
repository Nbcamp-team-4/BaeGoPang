package com.team.project.domain.payment.model.vo;

public enum PaymentStatus {
	READY, PAID, CANCEL_REQUESTED, CANCELED;

	// READY -> PAID -> CANCEL_REQUESTED -> CANCELED

	public boolean isReady() {
		return this == READY;
	}

	public boolean isInProgress() {
		return this == READY || this == CANCEL_REQUESTED;
	}
}
