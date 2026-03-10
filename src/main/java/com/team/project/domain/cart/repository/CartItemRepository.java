package com.team.project.domain.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.team.project.domain.cart.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from CartItem ci
        where ci.cart.id = :cartId
    """)
    void deleteAllByCartId(UUID cartId);
}