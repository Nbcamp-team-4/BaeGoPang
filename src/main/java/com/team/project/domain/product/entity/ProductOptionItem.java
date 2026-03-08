package com.team.project.domain.product.entity;

import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_product_option_item")
public class ProductOptionItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer additionalPrice;

    public static ProductOptionItem create(ProductOption productOption, String name, Integer additionalPrice) {
        ProductOptionItem item = new ProductOptionItem();
        item.productOption = productOption;
        item.name = name;
        item.additionalPrice = additionalPrice;
        return item;
    }

    public void update(String name, Integer additionalPrice) {
        if (name != null) {
            this.name = name;
        }
        if (additionalPrice != null) {
            this.additionalPrice = additionalPrice;
        }
    }

    public void delete(UUID deletedBy) {
        markDeleted(deletedBy);
    }
}