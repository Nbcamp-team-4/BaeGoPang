package com.team.project.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {

	@Query("""
			select distinct u
			from User u
			join fetch u.userRoles ur
			join fetch ur.role r
			where u.loginId = :loginId
		""")
	Optional<User> findByLoginIdWithRole(@Param("loginId") String loginId);

	Optional<User> findByLoginId(String loginId);

	Optional<User> findByEmail(String email);

	boolean existsByLoginId(String loginId);

	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
}