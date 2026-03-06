package com.team.project.domain.store.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com.team.project.domain.product.entity.Product;
import com.team.project.domain.region.entity.Region;
import com.team.project.domain.store.exception.InvalidDeliveryFeeException;
import com.team.project.domain.store.exception.InvalidDeliveryTimeException;
import com.team.project.domain.store.exception.InvalidMinimumOrderAmountException;
import com.team.project.domain.store.model.vo.StoreStatus;
import com.team.project.domain.store.service.command.CreateStoreCommand;
import com.team.project.domain.store.service.command.UpdateOwnerFieldsCommand;
import com.team.project.domain.store.service.command.UpdateStoreByAdminCommand;
import com.team.project.domain.user.entity.User;
import com.team.project.global.common.entity.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>(); // 명세서의 상품 테이블과 연결



	// === [생성 로직] 서비스에서 Store.create(command, user, region, location)으로 호출 ===
	// 정적 팩토리 메서드: 빌더 대신 "new"를 직접 컨트롤!
	public static Store create(CreateStoreCommand command, User user, Region region, Point location) {
		Store store = new Store();
		store.user = user;
		store.region = region;
		store.location = location;
		store.name = command.getName();
		store.description = command.getDescription();
		store.address = command.getAddress();
		store.phone = command.getPhone();
		store.imageUrl = command.getImageUrl();
		store.openTime = command.getOpenTime();
		store.closeTime = command.getCloseTime();
		store.deliveryMinMinutes = command.getDeliveryMinMinutes() != null ? command.getDeliveryMinMinutes() : 0;
		store.deliveryMaxMinutes = command.getDeliveryMaxMinutes() != null ? command.getDeliveryMaxMinutes() : 0;
		store.deliveryFee = command.getDeliveryFee() != null ? command.getDeliveryFee() : 0;
		store.minimumOrderAmount = command.getMinimumOrderAmount() != null ? command.getMinimumOrderAmount() : 0;
		store.status = StoreStatus.INACTIVE;
		return store;
	}

	// 1. OWNER용: 본인 매장 정보 수정
	public void updateByOwner(UpdateOwnerFieldsCommand command) {
		this.description = command.getDescription();
		this.phone = command.getPhone();

		// 이미지 처리 로직
		this.imageUrl = (command.getImageUrl() == null || command.getImageUrl().isBlank())
			? "https://raw.githubusercontent.com/.../default-store.png"
			: command.getImageUrl();

		this.openTime = command.getOpenTime();
		this.closeTime = command.getCloseTime();
		this.deliveryMinMinutes = command.getDeliveryMinMinutes();
		this.deliveryMaxMinutes = command.getDeliveryMaxMinutes();
		this.deliveryFee = command.getDeliveryFee();
		this.minimumOrderAmount = command.getMinimumOrderAmount();

		validateDeliveryTime();
		validateBusinessHours();
	}

	/*
	 * =========================
	 * 검증 로직 (필드 그룹화 대신 메서드로 깔끔하게)
	 * =========================
	 */

	// 2. ADMIN용: 모든 정보 수정 (Command와 변경된 Region을 통째로 받음)
	public void updateByAdmin(UpdateStoreByAdminCommand command, Region region) {
		// 관리자 전용 필드 및 지역 변경
		if (command.getName() != null)
			this.name = command.getName();
		if (command.getStatus() != null)
			this.status = command.getStatus();
		this.region = region;

		// 배달 및 운영 정보 업데이트 (Command에서 추출)
		this.deliveryMinMinutes = command.getDeliveryMinMinutes();
		this.deliveryMaxMinutes = command.getDeliveryMaxMinutes();
		this.deliveryFee = command.getDeliveryFee();
		this.minimumOrderAmount = command.getMinimumOrderAmount();

		this.description = command.getDescription();
		this.phone = command.getPhone();
		this.imageUrl = command.getImageUrl();
		this.openTime = command.getOpenTime();
		this.closeTime = command.getCloseTime();

		// 검증 로직 호출
		validateDeliveryTime();
		validateBusinessHours();
		validateFees();
	}

	private void validateFees() {
		if (this.deliveryFee != null && this.deliveryFee < 0)
			throw new InvalidDeliveryFeeException();
		if (this.minimumOrderAmount != null && this.minimumOrderAmount < 0)
			throw new InvalidMinimumOrderAmountException();
	}

	private void validateDeliveryTime() {
		if (this.deliveryMinMinutes != null && this.deliveryMaxMinutes != null) {
			if (this.deliveryMinMinutes > this.deliveryMaxMinutes) {
				throw new InvalidDeliveryTimeException();
			}
		}
	}

	private void validateBusinessHours() {
		if (this.openTime != null && this.closeTime != null) {
			if (!this.closeTime.isAfter(this.openTime)) {
				throw new InvalidDeliveryTimeException(); // 또는 별도의 InvalidBusinessHoursException
			}
		}
	}

	// 상태 변경 메서드들 (기존 유지)
	public void approve() {
		if (this.status != StoreStatus.INACTIVE) {
			throw new IllegalStateException("승인 대기 상태인 가게만 수락할 수 있습니다.");
		}
		this.status = StoreStatus.OPEN;
	}

	@Override
	public void markDeleted(UUID deletedBy) {
		// 1. 부모(BaseEntity) 로직으로 Store의 삭제 시간/삭제자 기록
		super.markDeleted(deletedBy);

		// 2. 가게 상태를 CLOSED로 변경 (운영 정책상 선택)
		this.status = StoreStatus.CLOSED;

		/* Todo-추후 상품 작업 끝나면 주석 해제
		/ 3. 연관된 모든 상품들도 함께 Soft Delete 전파
		if (this.products != null && !this.products.isEmpty()) {
			this.products.forEach(product -> product.markDeleted(deletedBy));
		}*/
	}
}