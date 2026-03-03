package com._team._project.domain.product.entity;

import java.time.LocalDateTime;
import java.util.UUID;

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
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
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

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;
    private UUID updatedBy;

    private LocalDateTime deletedAt;
    private UUID deletedBy;

    // ===== 생성 =====
    public Product(
        UUID storeId,
        String name,
        Integer price,
        String description,
        Boolean useAiDescription,
        String imageUrl,
        UUID userId
    ) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.useAiDescription = useAiDescription;
        this.imageUrl = imageUrl;
        this.isSoldOut = false;
        this.isHidden = false;
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
    }

    // ===== 수정 =====
    public void update(
        String name,
        Integer price,
        String description,
        Boolean useAiDescription,
        String imageUrl,
        UUID userId
    ) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (description != null) this.description = description;
        if (useAiDescription != null) this.useAiDescription = useAiDescription;
        if (imageUrl != null) this.imageUrl = imageUrl;

        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    // ===== 품절 =====
    public void markSoldOut(UUID userId) {
        this.isSoldOut = true;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    public void markAvailable(UUID userId) {
        this.isSoldOut = false;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    // ===== 숨김 =====
    public void hide(UUID userId) {
        this.isHidden = true;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    public void unhide(UUID userId) {
        this.isHidden = false;
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    // ===== 삭제 =====
    public void delete(UUID userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
    }
}