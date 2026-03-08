package com._team._project.domain.user.repository;

import com._team._project.domain.user.entity.Role;
import com._team._project.domain.user.entity.User;
import com._team._project.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, User> {

   Optional<UserRole> findByUser(User user);

    boolean existsByUserAndRole(User user, Role role);

    void deleteByUserAndRole(User user, Role role);
}
