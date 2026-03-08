package com.team.project.domain.user.entity;

import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_role")
@Getter
@NoArgsConstructor
public class Role extends BaseEntity {

	@Id
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
