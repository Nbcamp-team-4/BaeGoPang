package com.team.project.domain.user.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "p_user")
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
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

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserRole> userRoles = new ArrayList<>();

	public User(String loginId, String email, String password, String name, String phone) {
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}
}