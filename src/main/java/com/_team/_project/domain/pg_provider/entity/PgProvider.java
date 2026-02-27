package com._team._project.domain.pg_provider.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com._team._project.domain.pg_provider.model.vo.PgProviderStatus;
import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private PgProviderStatus status;

}
