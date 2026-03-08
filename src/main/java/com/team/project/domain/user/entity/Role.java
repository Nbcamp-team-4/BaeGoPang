package com.team.project.domain.user.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_role")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleType role;

	public static RoleType of(String role) {
		/**
		 * 해야 함
		 */
		return RoleType.valueOf(role);
	}
}
