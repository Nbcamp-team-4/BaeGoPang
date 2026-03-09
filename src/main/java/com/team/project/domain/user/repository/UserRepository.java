package com.team.project.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.team.project.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	@EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
	Optional<User> findByLoginId(String loginId);

	Optional<User> findById(UUID id);

	Optional<User> findByRefreshToken(String refreshToken);

	Optional<User> findByEmail(String email);

	boolean existsByLoginId(String loginId);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}