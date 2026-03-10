package com.team.project.domain.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, UUID>, CartRepositoryCustom {

    Optional<Cart> findByUserIdAndStatus(UUID userId, com.team.project.domain.cart.model.vo.CartStatus status);
}