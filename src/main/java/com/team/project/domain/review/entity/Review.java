package com.team.project.domain.review.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // UUID 해결을 위해 필수

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.team.project.domain.store.entity.Store; // Store 해결을 위해 필수

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Id 해결을 위해 필수
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table; // Table 해결을 위해 필수
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "p_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Review {

	@Id // 기본 키 에러 해결
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private UUID orderId;
	private UUID userId;

	// storeId 필드는 중복이라 삭제했습니다. (store_id 열 충돌 해결)

	private Integer rating;
	private String content;
	private Boolean isHidden;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Builder.Default
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
	private List<ReviewImage> reviewImages = new ArrayList<>();

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	@Column(updatable = false)
	private UUID createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private UUID updatedBy;

	private LocalDateTime deletedAt;
	private UUID deletedBy;

	public void update(Integer rating, String content) {
		this.rating = rating;
		this.content = content;
	}

	public void delete(UUID userId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = userId;
		this.isHidden = true;
	}
}