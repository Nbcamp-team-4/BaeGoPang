package com.team.project.domain.payment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;

import com.team.project.domain.payment.exception.InvalidPaymentRequestException;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

	// 추후 order과 연관관계
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "order_id")
	// private Order order;
	@Column(name = "order_id")
	private UUID order;

	@Builder
	public Payment(PaymentStatus status, Integer amount, String paymentKey, LocalDateTime paidAt, UUID order) {
		this.status = status;
		this.amount = amount;
		this.paymentKey = paymentKey;
		this.paidAt = paidAt;
		this.order = order;
	}

	public void pay(String paymentKey) {
		validateStatus(PaymentStatus.READY);
		this.status = PaymentStatus.PAID;
		this.paidAt = LocalDateTime.now();
		this.paymentKey = paymentKey;
	}

	public void requestCancel() {
		validateStatus(PaymentStatus.PAID);
		this.status = PaymentStatus.CANCEL_REQUESTED;
	}

	public void cancel() {
		validateStatus(PaymentStatus.CANCEL_REQUESTED);
		this.status = PaymentStatus.CANCELED;
	}

	public void markDeleted(UUID deletedBy) {
		super.markDeleted(deletedBy);
	}

	private void validateStatus(PaymentStatus expected) {
		if (this.status != expected) {
			throw new InvalidPaymentRequestException();
		}
	}

}
