package com.team.project.domain.store.entity;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import com.team.project.domain.category.entity.Category;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_store")
public class Store extends BaseEntity {

	/* ============================================================================
	 * 1. 필드 영역
	 * ============================================================================ */
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
	private StoreStatus status;

	@Column(nullable = false)
	private Integer deliveryMinMinutes;

	@Column(nullable = false)
	private Integer deliveryMaxMinutes;

	@Column(nullable = false)
	private Integer deliveryFee;

	@Column(nullable = false)
	private Integer minimumOrderAmount;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StoreCategory> storeCategories = new ArrayList<>();

	// 평점 과 리뷰
	// Store.java 내부 수정
	@Column(
		name = "average_rating",
		nullable = false,
		columnDefinition = "double precision default 0.0" // 'double' 대신 'double precision' 사용
	)
	private Double averageRating = 0.0;

	// 아래 두 필드는 표준이라 에러가 나지 않을 겁니다.
	@Column(name = "review_count", nullable = false)
	private Integer reviewCount = 0;

	@Column(name = "total_rating_sum", nullable = false)
	private Integer totalRatingSum = 0;

	/**
	 * 리뷰 등록 시 평점 갱신 (N+1 문제 해결)
	 */
	public void addReviewRating(int newRating) {
		this.totalRatingSum += newRating;
		this.reviewCount += 1;
		this.averageRating = (double) this.totalRatingSum / this.reviewCount;
	}
	/**
	 * 리뷰 삭제 시 평점 차감
	 */
	public void removeReviewRating(int oldRating) {
		if (this.reviewCount > 0) {
			this.totalRatingSum -= oldRating;
			this.reviewCount -= 1;
			this.averageRating = this.reviewCount > 0
				? (double) this.totalRatingSum / this.reviewCount
				: 0.0;
		}
	}
	/* ============================================================================
	 * 2. 생성자 및 정적 팩토리 메서드 영역 (1번 방식 핵심)
	 * ============================================================================ */

	// 빌더는 '필수 초기화가 필요한 필드'만 받도록 생성자에 지정합니다.
	@Builder
	public Store(User user, Region region, Point location, String name, String description,
		String address, String phone, String imageUrl, LocalTime openTime,
		LocalTime closeTime, Integer deliveryMinMinutes, Integer deliveryMaxMinutes,
		Integer deliveryFee, Integer minimumOrderAmount) {
		this.user = user;
		this.region = region;
		this.location = location;
		this.name = name;
		this.description = description;
		this.address = address;
		this.phone = phone;
		this.imageUrl = imageUrl;
		this.openTime = openTime;
		this.closeTime = closeTime;
		// null 방지 기본값 세팅
		this.deliveryMinMinutes = deliveryMinMinutes != null ? deliveryMinMinutes : 0;
		this.deliveryMaxMinutes = deliveryMaxMinutes != null ? deliveryMaxMinutes : 0;
		this.deliveryFee = deliveryFee != null ? deliveryFee : 0;
		this.minimumOrderAmount = minimumOrderAmount != null ? minimumOrderAmount : 0;
		// 신규 가게는 무조건 승인 대기 상태
		this.status = StoreStatus.INACTIVE;
	}

	// 서비스 계층에서 호출할 정적 팩토리 메서드 (내부에서 빌더 사용)
	public static Store create(CreateStoreCommand command, User user, Region region, Point location, List<Category> categories) {
		Store store = Store.builder()
			.user(user)
			.region(region)
			.location(location)
			.name(command.getName())
			.description(command.getDescription())
			.address(command.getAddress())
			.phone(command.getPhone())
			.imageUrl(command.getImageUrl())
			.openTime(command.getOpenTime())
			.closeTime(command.getCloseTime())
			.deliveryMinMinutes(command.getDeliveryMinMinutes())
			.deliveryMaxMinutes(command.getDeliveryMaxMinutes())
			.deliveryFee(command.getDeliveryFee())
			.minimumOrderAmount(command.getMinimumOrderAmount())
			.build();

		// 생성 직후 카테고리 매핑
		if (categories != null && !categories.isEmpty()) {
			store.addCategories(categories);
		}
		return store;
	}

	/* ============================================================================
	 * 3. 비즈니스 로직 (Public 메서드)
	 * ============================================================================ */

	// 1. OWNER용: 본인 매장 정보 수정
	public void updateByOwner(UpdateOwnerFieldsCommand command) {
		this.description = command.getDescription();
		this.phone = command.getPhone();

		// 이미지 처리 로직
		this.imageUrl = (command.getImageUrl() == null || command.getImageUrl().isBlank())
			? "https://raw.githubusercontent.com/.../default-store.png" // 실제 디폴트 URL로 교체 필요
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

	// 2. ADMIN용: 모든 정보 수정 (Command와 변경된 Region을 통째로 받음)
	public void updateByAdmin(UpdateStoreByAdminCommand command, Region region) {
		if (command.getName() != null) this.name = command.getName();
		if (command.getStatus() != null) this.status = command.getStatus();

		this.region = region;
		this.deliveryMinMinutes = command.getDeliveryMinMinutes();
		this.deliveryMaxMinutes = command.getDeliveryMaxMinutes();
		this.deliveryFee = command.getDeliveryFee();
		this.minimumOrderAmount = command.getMinimumOrderAmount();
		this.description = command.getDescription();
		this.phone = command.getPhone();
		this.imageUrl = command.getImageUrl();
		this.openTime = command.getOpenTime();
		this.closeTime = command.getCloseTime();

		validateDeliveryTime();
		validateBusinessHours();
		validateFees();
	}

	public void updateStatus(StoreStatus newStatus) {
		if (this.status == newStatus) return;

		// 공통 규칙: 이미 승인 대기인 상태로 되돌릴 수는 없음
		if (newStatus == StoreStatus.INACTIVE) {
			throw new IllegalStateException("승인 대기 상태로 되돌릴 수 없습니다.");
		}

		this.status = newStatus;
	}

	@Override
	public void markDeleted(UUID deletedBy) {
		// 1. 부모(BaseEntity) 로직으로 Store의 삭제 시간/삭제자 기록
		super.markDeleted(deletedBy);

		// 2. 가게 상태를 CLOSED로 변경
		this.status = StoreStatus.CLOSED;

       /* Todo-추후 상품 작업 끝나면 주석 해제
       if (this.products != null && !this.products.isEmpty()) {
          this.products.forEach(product -> product.markDeleted(deletedBy));
       }*/
	}


	/* ============================================================================
	 * 4. 연관관계 편의 메서드
	 * ============================================================================ */

	// 연관관계 편의 메서드 (Category 엔티티를 통째로 받아서 매핑 객체 생성 후 리스트에 추가)
	public void addCategories(List<Category> categories) {
		for (Category category : categories) {
			StoreCategory storeCategory = StoreCategory.builder()
				.store(this)
				.category(category)
				.build();
			this.storeCategories.add(storeCategory);
		}
	}

	// 카테고리 수정 (기존 매핑을 싹 비우고 새롭게 추가)
	public void updateCategories(List<Category> newCategories) {
		this.storeCategories.clear(); // orphanRemoval = true 덕분에 기존 데이터는 DB에서 자동 삭제됨!
		addCategories(newCategories); // 기존에 만들어둔 편의 메서드 재사용
	}

	/* ============================================================================
	 * 5. 내부 검증 로직 (Private 메서드)
	 * ============================================================================ */

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
				throw new InvalidDeliveryTimeException(); // 또는 InvalidBusinessHoursException
			}
		}
	}
}
