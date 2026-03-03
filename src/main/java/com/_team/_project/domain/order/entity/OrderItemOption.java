package com._team._project.domain.order.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_item_option")
@Getter
@NoArgsConstructor
public class OrderItemOption extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Column(name = "option_name", nullable = false, length = 100)
    private String optionName;

    @Column(name = "option_item_name", nullable = false, length = 100)
    private String optionItemName;

    @Column(name = "extra_price", nullable = false)
    private Integer extraPrice = 0;

    public OrderItemOption(String optionName, String optionItemName, Integer extraPrice) {
        this.optionName = optionName;
        this.optionItemName = optionItemName;
        this.extraPrice = (extraPrice == null) ? 0 : extraPrice;
    }

    void bindOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public void markDeleted(UUID deletedBy) {
        super.markDeleted(deletedBy);
    }
}