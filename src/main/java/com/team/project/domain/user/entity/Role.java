package com.team.project.domain.user.entity;

import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_role")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role  extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "role_type")
	private RoleType type;

	public Role(RoleType type) {
		this.type = type;
	}
}