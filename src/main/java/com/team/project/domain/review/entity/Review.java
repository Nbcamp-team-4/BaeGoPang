package com.team.project.domain.review.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.team.project.domain.store.entity.Store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@EntityListeners(AuditingEntityListener .class)
@Table(name = "p_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private UUID orderId;
	private UUID userId;
	//private UUID storeId;
	private Integer rating;
	private String content;
	private Boolean isHidden;


	// 이 부분이 있어야 review.getStore()를 사용할 수 있습니다.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	// 리뷰 이미지
	@Builder.Default
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
	private List<ReviewImage> reviewImages = new ArrayList<>();

	// Audit 필드 (실제로는 공통 클래스로 빼는 것이 좋지만, 우선 직접 추가)

	@CreatedDate // 2. 생성 시 시간 자동 기록
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;


	@CreatedBy // 생성 시 자동으로 AuditorAware에서 ID를 가져옴
	@Column(updatable = false) // 생성자는 수정되면 안 되니까 설정
	private UUID createdBy;

	@LastModifiedDate //시간
	private LocalDateTime updatedAt;


	// 수정 로직
	@LastModifiedBy // 수정 시 자동으로 ID 업데이트
	private UUID updatedBy;

	private LocalDateTime deletedAt;
	private UUID deletedBy;

	public void update(Integer rating, String content) {
		this.rating = rating;
		this.content = content;
	}

	// Review.java (Entity)
	public void delete(UUID userId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = userId;
		// 만약 isHidden 같은 상태값도 바꿔야 한다면 여기에 추가
		this.isHidden = true;
	}
}