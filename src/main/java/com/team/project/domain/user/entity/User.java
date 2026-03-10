package com.team.project.domain.user.entity;

import com.team.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
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

	public User(String loginId, String email, String password, String name, String phone, UserStatus status) {
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.status = status;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void clearRefreshToken() {
		this.refreshToken = null;
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

	public boolean isDeleted() {
		return this.getDeletedAt() != null;
	}

	public void softDelete(UUID deletedBy) {
		this.status = UserStatus.DELETED;
		this.markDeleted(deletedBy);
	}
	
	public List<String> getRoleNames() {
		return userRoles.stream()
			.map(userRole -> userRole.getRole().getType().name())
			.distinct()
			.toList();
	}

	public void addUserRole(UserRole userRole) {
		userRoles.add(userRole);
	}



}