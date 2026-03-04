package com._team._project.domain.review.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "p_review_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ReviewImage {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	@Column(nullable = false)
	private String imageUrl;


	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;
	@CreatedBy
	@Column(updatable = false)
	private UUID createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;
	@LastModifiedBy
	private UUID updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	@Column(name = "deleted_by")
	private UUID deletedBy;
	//삭제한사람 기록
	public void delete(UUID userId) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = userId;
	}

}