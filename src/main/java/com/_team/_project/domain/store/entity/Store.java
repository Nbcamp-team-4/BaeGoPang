package com._team._project.domain.store.entity;

import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com._team._project.domain.region.entity.Region;
import com._team._project.domain.store.exception.InvalidDeliveryFeeException;
import com._team._project.domain.store.exception.InvalidDeliveryTimeException;
import com._team._project.domain.store.exception.InvalidMinimumOrderAmountException;
import com._team._project.domain.store.model.vo.StoreStatus;
import com._team._project.domain.user.entity.User;
import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "p_store")
public class Store extends BaseEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", nullable = false)
	private Region region;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(columnDefinition = "text")
	private String description;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false, columnDefinition = "geometry(Point,4326)")
	private Point location;

	private String phone;

	private String imageUrl;

	private LocalTime openTime;

	private LocalTime closeTime;

	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "status", nullable = false)
	private StoreStatus status = StoreStatus.INACTIVE;

	@Column(nullable = false)
	private Integer deliveryMinMinutes;

	@Column(nullable = false)
	private Integer deliveryMaxMinutes;

	@Column(nullable = false)
	private Integer deliveryFee;

	@Column(nullable = false)
	private Integer minimumOrderAmount;

	public Store(User user, Region region, String name, String description, String address, Point location, String phone, String imageUrl, LocalTime openTime, LocalTime closeTime, Integer deliveryMinMinutes, Integer deliveryMaxMinutes, Integer deliveryFee, Integer minimumOrderAmount) {
		this.user = user;
		this.region = region;
		this.name = name;
		this.description = description;
		this.address = address;
		this.location = location;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.status = StoreStatus.INACTIVE;
		this.deliveryMinMinutes = deliveryMinMinutes != null ? deliveryMinMinutes : 0;
		this.deliveryMaxMinutes = deliveryMaxMinutes != null ? deliveryMaxMinutes : 0;
		this.deliveryFee = deliveryFee != null ? deliveryFee : 0;
		this.minimumOrderAmount = minimumOrderAmount != null ? minimumOrderAmount : 0;

		validateDeliveryTime();
		validateBusinessHours();
	}

	/*
	 * =========================
	 * 수정 로직 (권한별 분리)
	 * =========================
	 */

	// 1. OWNER용: 본인 매장 정보 수정
	public void updateByOwner(String description, String phone, String imageUrl, LocalTime openTime, LocalTime closeTime) {
		applyOwnerFields(description, phone, imageUrl, openTime, closeTime);
		validateBusinessHours();
	}

	// 2. ADMIN용: 모든 정보 수정 가능 (사장님 항목 포함)
	public void updateByAdmin(
		String name, StoreStatus status, Integer minMin, Integer maxMin, Integer fee, Integer minAmt,
		String description, String phone, String imageUrl, LocalTime openTime, LocalTime closeTime
	) {
		// 관리자 전용 필드
		if (name != null) this.name = name;
		if (status != null) this.status = status;
		if (minMin != null) this.deliveryMinMinutes = minMin;
		if (maxMin != null) this.deliveryMaxMinutes = maxMin;
		validateDeliveryTime();

		if (fee != null) {
			if (fee < 0) throw new InvalidDeliveryFeeException();
			this.deliveryFee = fee;
		}
		if (minAmt != null) {
			if (minAmt < 0) throw new InvalidMinimumOrderAmountException();
			this.minimumOrderAmount = minAmt;
		}

		// 사장님 필드도 함께 수정
		applyOwnerFields(description, phone, imageUrl, openTime, closeTime);
		validateBusinessHours();
	}

	/*
	 * =========================
	 * 검증 및 보조 로직
	 * =========================
	 */

	private void applyOwnerFields(String description, String phone, String imageUrl, LocalTime openTime, LocalTime closeTime) {
		if (description != null) this.description = description;
		if (phone != null) this.phone = phone;
		if (imageUrl != null) this.imageUrl = imageUrl;
		if (openTime != null) this.openTime = openTime;
		if (closeTime != null) this.closeTime = closeTime;
	}

	private void validateDeliveryTime() {
		if (this.deliveryMinMinutes > this.deliveryMaxMinutes) {
			throw new InvalidDeliveryTimeException();
		}
	}

	private void validateBusinessHours() {
		if (this.openTime != null && this.closeTime != null) {
			if (!this.closeTime.isAfter(this.openTime)) {
				throw new InvalidDeliveryTimeException();
			}
		}
	}

	public void approve() { this.status = StoreStatus.OPEN; }
	public void close() { this.status = StoreStatus.CLOSED; }
	public void reopen() { this.status = StoreStatus.OPEN; }
	public void delete(UUID userId) { this.markDeleted(userId); }
}