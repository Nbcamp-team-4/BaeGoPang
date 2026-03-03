package com.team.project.review.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener .class)
@Table(name = "p_review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder // 이 어노테이션이 있어야 .builder()를 사용 가능합니다.
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	private UUID orderId;
	private UUID userId;
	private UUID storeId;
	private Integer rating;
	private String content;
	private Boolean isHidden;

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

	// Soft Delete 로직
	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
		// 삭제한 사용자 ID는 AuditorAware나 SecurityContext에서 주입받아야 함
	}
}