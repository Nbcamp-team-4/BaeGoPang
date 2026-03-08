package com._team._project.domain.user.repository;

import com._team._project.domain.user.entity.Role;
import com._team._project.domain.user.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByRole(RoleType role);
}
