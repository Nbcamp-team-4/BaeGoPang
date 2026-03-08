package com.team.project.domain.user.entity;

import java.util.UUID;

import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	private Role role;

	public static UserRole create(User user, Role role) {
		UserRole userRole = new UserRole();
		userRole.user = user;
		userRole.role = role;
		return userRole;
	}
}