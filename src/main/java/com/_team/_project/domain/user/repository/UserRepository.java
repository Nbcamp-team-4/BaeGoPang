package com._team._project.domain.user.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
}