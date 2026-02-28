package com._team._project.domain.region.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;
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
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @JdbcTypeCode(SqlTypes.GEOMETRY)
    @Column(nullable = false, columnDefinition = "geometry(MultiPolygon,4326)")
    private MultiPolygon geom;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // 생성용 생성자
    public Region(String name, MultiPolygon geom, boolean isActive) {
        this.name = name;
        this.geom = geom;
        this.isActive = isActive;
    }
    public void update(String name, MultiPolygon geom, boolean isActive) {
        this.name = name;
        this.geom = geom;
        this.isActive = isActive;
    }
}