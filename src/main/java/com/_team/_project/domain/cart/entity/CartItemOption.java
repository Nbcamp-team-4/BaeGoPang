package com._team._project.domain.cart.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com._team._project.domain.product.entity.ProductOption;
import com._team._project.domain.product.entity.ProductOptionItem;
import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_cart_item_option")
@Getter
@NoArgsConstructor
public class CartItemOption extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_item_id", nullable = false)
    private ProductOptionItem productOptionItem;

    public CartItemOption(ProductOption productOption, ProductOptionItem productOptionItem) {
        this.productOption = productOption;
        this.productOptionItem = productOptionItem;
    }

    protected void assignCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
}