package com.team.project.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.RoleType;

public interface RoleRepository extends JpaRepository<Role, UUID> {
	Optional<Role> findByRole(RoleType role);
}
