package com.team.project.domain.user.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Builder;
import org.hibernate.annotations.UuidGenerator;

import com.team.project.global.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(nullable = false, unique = true)
	private String loginId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.ACTIVE;

	@Column(length = 500)
	private String refreshToken;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<UserRole> userRoles = new ArrayList<>();

	public User(String loginId, String email, String password, String name, String phone) {
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void clearRefreshToken() {
		this.refreshToken = null;
	}

	public List<String> getRoleNames() {
		return userRoles.stream()
			.map(userRole -> userRole.getRole().getRole().name())
			.distinct()
			.toList();
	}

	@Builder
	private User(
			UUID id,
			String loginId,
			String email,
			String password,
			String name,
			String phone,
			UserStatus status
	) {
		this.id = id;
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.status = status;
	}

	public void addUserRole(UserRole userRole) {
		this.userRoles.add(userRole);
	}

	public void updateInfo(String email, String name, String phone) {
		if (email != null && !email.isBlank()) {
			this.email = email;
		}
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
		if (phone != null && !phone.isBlank()) {
			this.phone = phone;
		}
	}

	public void softDelete(UUID userId) {
		this.status = UserStatus.DELETED;
	}
	public boolean isDeleted() {
		return getDeletedAt() != null || this.status == UserStatus.DELETED;
	}
}