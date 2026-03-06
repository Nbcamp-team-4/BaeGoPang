package com.team.project.domain.payment.model.vo;

public enum PaymentStatus {
	READY,
	PENDING,
	COMPLETED,
	FAILED,
	CANCEL_REQUESTED,
	CANCELED,
	REFUND_FAILED;

	// READY -> PAID -> CANCEL_REQUESTED -> CANCELED

	public boolean isReady() {
		return this == READY;
	}

	public boolean isInProgress() {
		return this == READY || this == CANCEL_REQUESTED;
	}
}
