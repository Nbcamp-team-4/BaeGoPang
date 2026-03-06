package com.team.project.domain.order.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.UuidGenerator;

import com.team.project.domain.product.entity.Product;
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
@Table(name = "p_order_item")
@Getter
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "unit_price", nullable = false)
    private Integer unitPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "line_total_amount", nullable = false)
    private Integer lineTotalAmount;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemOption> options = new LinkedHashSet<>();

    public OrderItem(Product product, String productName, Integer unitPrice, Integer quantity) {
        this.product = product;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.lineTotalAmount = unitPrice * quantity;
    }

    void bindOrder(Order order) {
        this.order = order;
    }

    public void addOption(OrderItemOption option) {
        option.bindOrderItem(this);
        this.options.add(option);
    }

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
        this.lineTotalAmount = this.unitPrice * quantity;
    }

    public void markDeleted(UUID deletedBy) {
        super.markDeleted(deletedBy);
    }
}