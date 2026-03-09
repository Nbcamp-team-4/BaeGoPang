package com.team.project.domain.product.entity;

import java.util.ArrayList;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_product_option")
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //옵션 아이템 리스트
    @OneToMany(mappedBy = "productOption", fetch = FetchType.LAZY)
    private List<ProductOptionItem> optionItems = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private boolean isRequired;

    public static ProductOption create(Product product, String name, boolean isRequired) {
        ProductOption productOption = new ProductOption();
        productOption.product = product;
        productOption.name = name;
        productOption.isRequired = isRequired;
        return productOption;
    }

    public void update(String name, Boolean isRequired) {
        if (name != null) {
            this.name = name;
        }
        if (isRequired != null) {
            this.isRequired = isRequired;
        }
    }

    public void delete(UUID deletedBy) {
        markDeleted(deletedBy);
    }
}