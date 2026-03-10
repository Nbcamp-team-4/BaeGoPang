package com.team.project.domain.cart.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.team.project.domain.cart.entity.CartItemOption;

public interface CartItemOptionRepository extends JpaRepository<CartItemOption, UUID> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from CartItemOption cio
        where cio.cartItem.cart.id = :cartId
    """)
    void deleteAllByCartId(UUID cartId);
}