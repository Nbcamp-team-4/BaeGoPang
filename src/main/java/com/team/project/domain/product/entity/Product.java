package com.team.project.domain.product.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.team.project.domain.store.entity.Store;
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
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private List<ProductOption> options = new ArrayList<>();

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private boolean useAiDescription;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean isSoldOut;

    @Column(nullable = false)
    private boolean isHidden;

    // ===== 생성 =====
    public static Product create(
        Store store,
        String name,
        Integer price,
        String description,
        boolean useAiDescription,
        String imageUrl
    ) {
        Product product = new Product();
        product.store = store;
        product.name = name;
        product.price = price;
        product.description = description;
        product.useAiDescription = useAiDescription;
        product.imageUrl = imageUrl;
        product.isSoldOut = false;
        product.isHidden = false;
        return product;
    }
    // ===== 수정 =====
    public void update(
        String name,
        Integer price,
        String description,
        Boolean useAiDescription,
        String imageUrl
    ) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (description != null) this.description = description;
        if (useAiDescription != null) this.useAiDescription = useAiDescription;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    // ===== 품절 =====
    public void markSoldOut() {
        this.isSoldOut = true;
    }

    public void markAvailable() {
        this.isSoldOut = false;
    }

    // ===== 숨김 =====
    public void hide() {
        this.isHidden = true;
    }

    public void unhide() {
        this.isHidden = false;
    }

    // ===== 삭제 =====
    public void delete(UUID deletedBy) {
        markDeleted(deletedBy);
    }
}