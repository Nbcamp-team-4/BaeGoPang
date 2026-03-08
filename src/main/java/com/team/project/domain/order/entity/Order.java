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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order")
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

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
        this.status = OrderStatus.PENDING_PAYMENT;
        this.orderDate = LocalDateTime.now();
    }

    // ====== 연관 편의 메서드 ======
    public void addItem(OrderItem item) {
        item.bindOrder(this);
        this.items.add(item);
    }

    // ====== 도메인 메서드 ======

    /**
     * 결제 완료 처리
     * - 주문 생성 직후(PENDING_PAYMENT) 상태에서만 결제 완료로 변경 가능
     */
    public void markPaid() {
        validateStatus(OrderStatus.PENDING_PAYMENT);
        this.status = OrderStatus.PAID;
    }

    /**
     * 가게 주문 수락 처리
     * - 결제 완료(PAID) 상태에서만 수락 가능
     */
    public void accept() {
        validateStatus(OrderStatus.PAID);
        this.status = OrderStatus.ACCEPTED;
    }

    /**
     * 가게 주문 거절 처리
     * - 결제 완료(PAID) 상태에서만 거절 가능
     * - 현재 별도 rejectedReason 컬럼이 없으므로 canceledReason 컬럼을 임시 재사용
     */
    public void reject(String reason) {
        validateStatus(OrderStatus.PAID);
        this.status = OrderStatus.REJECTED;
        this.canceledReason = reason;
    }

    /**
     * 조리 시작 처리
     * - 수락(ACCEPTED) 상태에서만 조리중(COOKING)으로 변경 가능
     */
    public void startCooking() {
        validateStatus(OrderStatus.ACCEPTED);
        this.status = OrderStatus.COOKING;
    }

    /**
     * 배달 시작 처리
     * - 조리중(COOKING) 상태에서만 배달중(DELIVERING)으로 변경 가능
     */
    public void startDelivering() {
        validateStatus(OrderStatus.COOKING);
        this.status = OrderStatus.DELIVERING;
    }

    /**
     * 주문 완료 처리
     * - 배달중(DELIVERING) 상태에서만 완료(COMPLETED) 처리 가능
     */
    public void complete() {
        validateStatus(OrderStatus.DELIVERING);
        this.status = OrderStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    /**
     * 주문 취소 처리
     * - 현재 정책상 결제대기 / 결제완료 / 수락 상태까지만 취소 허용
     * - 조리중 이후 취소 정책은 팀 룰에 맞게 추후 조정 가능
     */
    public void cancel(String reason) {
        validateStatus(OrderStatus.PENDING_PAYMENT, OrderStatus.PAID, OrderStatus.ACCEPTED);
        this.status = OrderStatus.CANCELED;
        this.canceledReason = reason;
    }

    public void markDeleted(UUID deletedBy) {
        super.markDeleted(deletedBy);
    }

    /**
     * 현재 주문 상태가 허용된 상태 중 하나인지 검증
     */
    private void validateStatus(OrderStatus... allowedStatuses) {
        for (OrderStatus allowedStatus : allowedStatuses) {
            if (this.status == allowedStatus) {
                return;
            }
        }
        throw new IllegalStateException("허용되지 않은 주문 상태 변경입니다. currentStatus=" + this.status);
    }
}