package com._team._project.domain.pg_provider.entity;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com._team._project.domain.pg_provider.api.request.UpdatePgProviderRequest;
import com._team._project.domain.pg_provider.exception.InvalidInputException;
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
	@Column(name = "id", updatable = false, nullable = false)
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

	public void markDeleted(UUID deletedBy) {
		super.markDeleted(deletedBy);
		this.status = PgProviderStatus.DEACTIVE;
	}

	public boolean update(UpdatePgProviderRequest request) {
		if (request == null) {
			return false;
		}
		boolean b1 = updateCode(request);
		b1 = b1 && updateName(request);
		b1 = b1 && updateStatus(request);
		return b1;
	}

	private boolean updateStatus(UpdatePgProviderRequest request) {
		if (request.getStatus() != null) {
			if (request.getStatus() == PgProviderStatus.DEACTIVE)
				throw new InvalidInputException();

			this.status = request.getStatus();
			return true;
		}
		return false;
	}

	private boolean updateName(UpdatePgProviderRequest request) {
		if (request.getName() != null) {
			int length = request.getName().length();
			if (length <= 0 || length > 50)
				throw new InvalidInputException();

			this.name = request.getName();
			return true;
		}
		return false;
	}

	private boolean updateCode(UpdatePgProviderRequest request) {
		if (request.getCode() != null) {
			int length = request.getCode().length();
			if (length <= 0 || length > 50)
				throw new InvalidInputException();

			this.code = request.getCode();
			return true;
		}
		return false;
	}
}


