package com.team.project.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByLoginId(String loginId);

	Optional<User> findByEmail(String email);
}