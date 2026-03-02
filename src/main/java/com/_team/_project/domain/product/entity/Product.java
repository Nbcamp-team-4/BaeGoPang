package com._team._project.domain.product.entity;

import com._team._project.domain.product.entity.ProductStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // store 연동 필요함 (2차: FK/연관관계)
    @Column(name = "store_id", columnDefinition = "uuid", nullable = false)
    private UUID storeId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, precision = 12, scale = 0)
    private BigDecimal price;

    // 상품 설명: AI 담당자 연동 (2차)
    @Column(length = 2000)
    private String description;

    @Column(name = "is_sold_out", nullable = false)
    private boolean soldout = false;

    /*
     * 상품 숨김 처리
     */
    @Column(name = "is_hidden", nullable = false)
    private boolean hidden = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    // Soft Delete
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Product(UUID storeId, String name, BigDecimal price, String description) {
        this.storeId = storeId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.hidden = false;
        this.soldout =false;
        this.status = ProductStatus.ACTIVE;
    }

    public void updateInfo(String name, BigDecimal price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void hide() {
        this.hidden = true;
        this.soldout =true;
    }

    public void unhide() {
        this.hidden = false;
        this.soldout = false;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    public void activate() {
        this.status = ProductStatus.ACTIVE;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
