package com._team._project.domain.pg_provider.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;
import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_pg_provider")
@Getter
@NoArgsConstructor
public class PgProvider extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", columnDefinition = "BINARY(16)", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "status", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private PgProviderStatus status = PgProviderStatus.ACTIVE;

	@Builder
	public PgProvider(String code, String name, PgProviderStatus status) {
		this.code = code;
		this.name = name;
		this.status = status;
	}
}
