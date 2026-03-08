package com.team.project.domain.user.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
	@ColumnDefault("ACTIVE")
	private UserStatus status;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<UserRole> userRoles = new ArrayList<>();

	public void changePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void updateProfile(String name, String phone) {
		if (name != null && !name.isBlank()) this.name = name;
		if (phone != null && !phone.isBlank()) this.phone = phone;
	}

	public void changeStatus(UserStatus status) {
		this.status = status;
	}
	public User(String loginId, String email, String password, String name, String phone, UserStatus status) {
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.status = status;
	}


}