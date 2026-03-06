package com.team.project.domain.cart.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.team.project.domain.cart.model.vo.CartStatus;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.user.entity.User;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_cart")
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // 유저당 ACTIVE 카트 1개 정책
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 한 장바구니 = 한 가게
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private CartStatus status = CartStatus.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart(User user, Store store) {
        this.user = user;
        this.store = store;
        this.status = CartStatus.ACTIVE;
    }

    // ======================
    // 비즈니스 메서드
    // ======================

    // 장바구니 가게 변경 정책: 다른 가게 상품 담기면 기존 장바구니 비우고 store 교체
    public void changeStoreAndClear(Store newStore) {
        this.store = newStore;
        this.items.clear(); // orphanRemoval=true 이라 cart_item, option도 같이 정리됨
    }

    public void addItem(CartItem item) {
        this.items.add(item);
        item.assignCart(this);
    }

    public void clearItems() {
        this.items.clear();
    }

    public void markOrdered() {
        this.status = CartStatus.ORDERED;
    }

    public void abandon() {
        this.status = CartStatus.ABANDONED;
    }

    public void markDeleted(UUID deletedBy) {
        super.markDeleted(deletedBy);
    }
}