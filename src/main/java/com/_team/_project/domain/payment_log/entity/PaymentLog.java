package com._team._project.domain.payment_log.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com._team._project.domain.payment.entity.Payment;
import com._team._project.domain.payment_log.model.vo.PaymentLogStatus;
import com._team._project.domain.pg_provider.entity.PgProvider;
import com._team._project.global.common.entity.BaseEntity;

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

	@Column(name = "p_tid")
	private String tid;

	@Column(name = "status", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PaymentLogStatus status;

	@Column(name = "reason")
	@Lob
	private String reason;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "payment_id", nullable = false)
	private Payment payment;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "pg_provider_id", nullable = true)
	private PgProvider pgProvider;

	@Builder
	public PaymentLog(String tid, PaymentLogStatus status, String reason, Payment payment, PgProvider pgProvider) {
		this.tid = tid;
		this.status = status;
		this.reason = reason;
		this.payment = payment;
		this.pgProvider = pgProvider;
	}
}
