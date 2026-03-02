package com._team._project.domain.region.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.MultiPolygon;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(nullable = false, columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon geom;

    /**
     * 비활성화 운영용 컬럼
     * - true  : 활성(기본 조회 대상)
     * - false : 비활성(일반 조회에서 제외)
     */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // 생성(기본 active=true)
    public Region(String name, MultiPolygon geom) {
        this.name = name;
        this.geom = geom;
        this.active = true;
    }

    // 정보 수정
    public void updateInfo(String name, MultiPolygon geom) {
        this.name = name;
        this.geom = geom;
    }

    // 비활성화
    public void deactivate() {
        this.active = false;
    }

    // 활성화
    public void activate() {
        this.active = true;
    }
}