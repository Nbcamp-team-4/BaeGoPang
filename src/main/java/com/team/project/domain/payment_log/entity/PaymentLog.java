package com.team.project.domain.payment_log.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment_log.model.vo.PaymentLogStatus;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_payment_log")
@Getter
@NoArgsConstructor
public class PaymentLog extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "payment_key")
	private String paymentKey;

	@Column(name = "status", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PaymentLogStatus status;

	@Column(name = "reason", columnDefinition = "text")
	private String reason;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "payment_id", nullable = false)
	private Payment payment;

	@Builder
	public PaymentLog(String paymentKey, PaymentLogStatus status, String reason, Payment payment) {
		this.paymentKey = paymentKey;
		this.status = status;
		this.reason = reason;
		this.payment = payment;
	}

	public void markDeleted(UUID deletedBy) {
		super.markDeleted(deletedBy);
	}
}
