package com.team.project.domain.product.entity;

import java.util.UUID;

import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "store_id", columnDefinition = "uuid", nullable = false)
    private UUID storeId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Boolean useAiDescription;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean isSoldOut;

    @Column(nullable = false)
    private Boolean isHidden;

    // ===== 생성 =====
    public Product(
        UUID storeId,
        String name,
        Integer price,
        String description,
        Boolean useAiDescription,
        String imageUrl
    ) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.useAiDescription = useAiDescription;
        this.imageUrl = imageUrl;
        this.isSoldOut = false;
        this.isHidden = false;
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
    public void delete(UUID userId) {
        markDeleted(userId);
    }
}