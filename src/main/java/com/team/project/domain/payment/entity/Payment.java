package com.team.project.domain.payment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.payment.exception.InvalidPaymentRequestException;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_payment")
@Getter
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
public class Payment extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "status", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PaymentStatus status = PaymentStatus.READY;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	@Column(name = "payment_key")
	private String paymentKey;

	private LocalDateTime paidAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@Builder
	public Payment(PaymentStatus status, Integer amount, String paymentKey, LocalDateTime paidAt, Order order) {
		this.status = status;
		this.amount = amount;
		this.paymentKey = paymentKey;
		this.paidAt = paidAt;
		this.order = order;
	}

	/**
	 * 결제 승인 대기 상태로 변경
	 * - 외부 PG 승인 직전/직후 중간 상태가 필요할 때 사용
	 * - 현재 서비스에서 바로 승인 완료 처리한다면 사용하지 않아도 됨
	 */
	public void markPending() {
		validateStatus(PaymentStatus.READY);
		this.status = PaymentStatus.PENDING;
	}

	/**
	 * 결제 완료 처리
	 * - READY 또는 PENDING 상태에서만 완료(COMPLETED) 처리 가능
	 */
	public void complete(String paymentKey) {
		validateStatus(PaymentStatus.READY, PaymentStatus.PENDING);
		this.status = PaymentStatus.COMPLETED;
		this.paidAt = LocalDateTime.now();
		this.paymentKey = paymentKey;
	}

	/**
	 * 결제 실패 처리
	 * - READY 또는 PENDING 상태에서만 실패 처리 가능
	 */
	public void fail() {
		validateStatus(PaymentStatus.READY, PaymentStatus.PENDING);
		this.status = PaymentStatus.FAILED;
	}

	/**
	 * 결제 취소 요청 처리
	 * - 결제 완료(COMPLETED) 상태에서만 취소 요청 가능
	 */
	public void requestCancel() {
		validateStatus(PaymentStatus.COMPLETED);
		this.status = PaymentStatus.CANCEL_REQUESTED;
	}

	/**
	 * 결제 취소 완료 처리
	 * - 취소 요청(CANCEL_REQUESTED) 상태에서만 취소 완료 가능
	 */
	public void cancel() {
		validateStatus(PaymentStatus.CANCEL_REQUESTED);
		this.status = PaymentStatus.CANCELED;
	}

	/**
	 * 환불 실패 처리
	 * - 취소 요청(CANCEL_REQUESTED) 상태에서만 환불 실패 처리 가능
	 */
	public void markRefundFailed() {
		validateStatus(PaymentStatus.CANCEL_REQUESTED);
		this.status = PaymentStatus.REFUND_FAILED;
	}

	public void markDeleted(UUID deletedBy) {
		super.markDeleted(deletedBy);
	}

	/**
	 * 현재 결제 상태가 허용된 상태 중 하나인지 검증
	 */
	private void validateStatus(PaymentStatus... allowedStatuses) {
		for (PaymentStatus allowedStatus : allowedStatuses) {
			if (this.status == allowedStatus) {
				return;
			}
		}
		throw new InvalidPaymentRequestException();
	}
}