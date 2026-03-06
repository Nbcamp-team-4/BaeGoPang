package com.team.project.domain.store.entity;

import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_store")
public class Store extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	// FK → p_user.id
	@Column(name = "user_id", columnDefinition = "uuid", nullable = false)
	private UUID userId;

	// FK → p_region.id
	@Column(name = "region_id", columnDefinition = "uuid", nullable = false)
	private UUID regionId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(columnDefinition = "text")
	private String description;

	@Column(nullable = false, length = 255)
	private String address;

	// PostGIS 위치 좌표
	@Column(nullable = false, columnDefinition = "geometry(Point,4326)")
	private Point location;

	private String phone;

	private String imageUrl;

	private LocalTime openTime;

	private LocalTime closeTime;

	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "status", nullable = false)
	private StoreStatus status = StoreStatus.OPEN;

	@Column(nullable = false)
	private Integer deliveryMinMinutes;

	@Column(nullable = false)
	private Integer deliveryMaxMinutes;

	@Column(nullable = false)
	private Integer deliveryFee;

	@Column(nullable = false)
	private Integer minimumOrderAmount;

	/*
	 * =========================
	 * 생성
	 * =========================
	 */
	public Store(
		UUID userId,
		UUID regionId,
		String name,
		String description,
		String address,
		Point location,
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
		this.location = location;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.openTime = openTime;
		this.closeTime = closeTime;

		this.status = StoreStatus.OPEN;

		this.deliveryMinMinutes = deliveryMinMinutes;
		this.deliveryMaxMinutes = deliveryMaxMinutes;
		this.deliveryFee = deliveryFee == null ? 0 : deliveryFee;
		this.minimumOrderAmount = minimumOrderAmount == null ? 0 : minimumOrderAmount;
	}

	/*
	 * =========================
	 * 수정
	 * =========================
	 */
	public void update(
		String name,
		String description,
		String address,
		String phone,
		String imageUrl,
		LocalTime openTime,
		LocalTime closeTime,
		StoreStatus status,
		Integer deliveryMinMinutes,
		Integer deliveryMaxMinutes,
		Integer deliveryFee,
		Integer minimumOrderAmount
	) {

		if (name != null)
			this.name = name;
		if (description != null)
			this.description = description;
		if (address != null)
			this.address = address;
		if (phone != null)
			this.phone = phone;
		if (imageUrl != null)
			this.imageUrl = imageUrl;
		if (openTime != null)
			this.openTime = openTime;
		if (closeTime != null)
			this.closeTime = closeTime;
		if (status != null)
			this.status = status;

		if (deliveryMinMinutes != null)
			this.deliveryMinMinutes = deliveryMinMinutes;
		if (deliveryMaxMinutes != null)
			this.deliveryMaxMinutes = deliveryMaxMinutes;
		if (deliveryFee != null)
			this.deliveryFee = deliveryFee;
		if (minimumOrderAmount != null)
			this.minimumOrderAmount = minimumOrderAmount;
	}

	/*
	 * =========================
	 * Soft Delete
	 * =========================
	 */
	public void delete(UUID userId) {
		this.status = StoreStatus.INACTIVE;
		this.markDeleted(userId);
	}

}