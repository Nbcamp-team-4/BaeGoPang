package com._team._project.domain.store.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "uuid", nullable = false)
    private UUID userId;

    @Column(name = "region_id", columnDefinition = "uuid", nullable = false)
    private UUID regionId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    //private Point location;

    private String phone;
    private String imageUrl;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @Column(nullable = false)
    private Integer deliveryMinMinutes;

    @Column(nullable = false)
    private Integer deliveryMaxMinutes;

    @Column(nullable = false)
    private Integer deliveryFee;

    @Column(nullable = false)
    private Integer minimumOrderAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private UUID createdBy;

    private LocalDateTime updatedAt;
    private UUID updatedBy;

    private LocalDateTime deletedAt;
    private UUID deletedBy;

    // 생성
    public Store(
        UUID userId,
        UUID regionId,
        String name,
        String description,
        String address,
        //Point location,
        String phone,
        String imageUrl,
        LocalTime openTime,
        LocalTime closeTime,
        Integer deliveryMinMinutes,
        Integer deliveryMaxMinutes,
        Integer deliveryFee,
        Integer minimumOrderAmount
    ) {
        this.userId = userId;
        this.regionId = regionId;
        this.name = name;
        this.description = description;
        this.address = address;
        //this.location = location;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = StoreStatus.OPEN;
        this.deliveryMinMinutes = deliveryMinMinutes;
        this.deliveryMaxMinutes = deliveryMaxMinutes;
        this.deliveryFee = deliveryFee == null ? 0 : deliveryFee;
        this.minimumOrderAmount = minimumOrderAmount == null ? 0 : minimumOrderAmount;
        this.createdAt = LocalDateTime.now();
        this.createdBy = userId;
    }

    // 수정
    public void update(
        String name,
        String description,
        String address,
        //Point location,
        String phone,
        String imageUrl,
        LocalTime openTime,
        LocalTime closeTime,
        StoreStatus status,
        Integer deliveryMinMinutes,
        Integer deliveryMaxMinutes,
        Integer deliveryFee,
        Integer minimumOrderAmount,
        UUID userId
    ) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;
        if (address != null) this.address = address;
        //if (location != null) this.location = location;
        if (phone != null) this.phone = phone;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (openTime != null) this.openTime = openTime;
        if (closeTime != null) this.closeTime = closeTime;
        if (status != null) this.status = status;
        if (deliveryMinMinutes != null) this.deliveryMinMinutes = deliveryMinMinutes;
        if (deliveryMaxMinutes != null) this.deliveryMaxMinutes = deliveryMaxMinutes;
        if (deliveryFee != null) this.deliveryFee = deliveryFee;
        if (minimumOrderAmount != null) this.minimumOrderAmount = minimumOrderAmount;

        this.updatedAt = LocalDateTime.now();
        this.updatedBy = userId;
    }

    // 삭제 (Soft)
    public void delete(UUID userId) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = userId;
        this.status = StoreStatus.INACTIVE;
    }
}