package com.team.project.domain.order.entity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.store.entity.Store;
import com.team.project.domain.user.entity.User;
import com.team.project.domain.user.entity.UserAddress;
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
@Table(name = "p_order")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id")
    private UserAddress deliveryAddress;

    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "request_memo", length = 255)
    private String requestMemo;

    @Column(name = "canceled_reason", length = 255)
    private String canceledReason;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> items = new LinkedHashSet<>();

    // ====== 생성 로직 (CRUD용) ======
    public Order(User user, Store store, UserAddress deliveryAddress,
                 String orderNo, Integer totalAmount, String requestMemo) {
        this.user = user;
        this.store = store;
        this.deliveryAddress = deliveryAddress;
        this.orderNo = orderNo;
        this.totalAmount = totalAmount;
        this.requestMemo = requestMemo;
        this.status = OrderStatus.PENDING;
        this.orderDate = LocalDateTime.now();
    }

    // ====== 연관 편의 메서드 ======
    public void addItem(OrderItem item) {
        item.bindOrder(this);
        this.items.add(item);
    }

    // ====== 도메인 메서드 ======
    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    public void cancel(String reason) {
        this.status = OrderStatus.CANCELED;
        this.canceledReason = reason;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void markDeleted(UUID deletedBy) {
        super.markDeleted(deletedBy);
    }
}