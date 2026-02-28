package com._team._project.domain.payment.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com._team._project.domain.payment.model.vo.PaymentMethod;
import com._team._project.domain.payment.model.vo.PaymentStatus;
import com._team._project.global.common.entity.BaseEntity;

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
public class Payment extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "method", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PaymentMethod method = PaymentMethod.CARD;

	@Column(name = "status", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PaymentStatus status = PaymentStatus.READY;

	@Column(name = "amount", nullable = false)
	private Integer amount;

	private LocalDateTime paidAt;

	// 추후 order과 연관관계
	@Column(name = "order_id", nullable = false)
	private UUID order;

	@Builder
	public Payment(PaymentMethod method, PaymentStatus status, Integer amount, LocalDateTime paidAt, UUID order) {
		this.method = method;
		this.status = status;
		this.amount = amount;
		this.paidAt = paidAt;
		this.order = order;
	}

}
