package com._team._project.domain.store.entity;

import com._team._project.domain.store.entity.StoreStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
//import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    // owner/user 연동 필요함
    //@Column(name = "owner_id", columnDefinition = "uuid")
    //private UUID ownerId;

    // region 연동 필요함 (2차: Region 포함 검증 + FK/연관관계)
    @Column(name = "region_id", columnDefinition = "uuid")
    private UUID regionId;

    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 가게 좌표 (경도/위도)
     * - SRID 4326

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point location;
     */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoreStatus status = StoreStatus.ACTIVE;

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

    /*public Store(String name, Point location) {
        this.name = name;
        this.location = location;
        this.status = StoreStatus.ACTIVE;
    }

    public void updateInfo(String name, Point location) {
        this.name = name;
        this.location = location;
    }
    */
    public void deactivate() {
        this.status = StoreStatus.INACTIVE;
    }

    public void activate() {
        this.status = StoreStatus.ACTIVE;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
