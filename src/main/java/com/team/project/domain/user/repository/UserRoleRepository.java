package com.team.project.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.user.entity.Role;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, User> {

	List<UserRole> findByUser(User user);

	boolean existsByUserAndRole(User user, Role role);

	void deleteByUserAndRole(User user, Role role);
}
