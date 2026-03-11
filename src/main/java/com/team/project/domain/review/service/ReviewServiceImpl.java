package com.team.project.domain.review.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.request.PageReviewRequest;
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;
import com.team.project.domain.review.entity.Review;
import com.team.project.domain.review.entity.ReviewImage;
import com.team.project.domain.review.repository.ReviewImageRepository;
import com.team.project.domain.review.repository.ReviewRepository;
import com.team.project.global.file.ImageType;
import com.team.project.global.file.service.ImageService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final OrderRepository orderRepository;

	// 1. 이미지 인터페이스 주입 (설정에 따라 Local/S3 자동 선택)
	private final ImageService imageService;

	@Override
	@Transactional
	public ReviewResponse createReview(
		UUID orderId,
		UUID userId,
		CreateReviewRequest request,
		List<MultipartFile> images) {

		// 1. 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

		// 2. 검증 로직 (권한, 상태, 중복)
		if (!order.getUser().getId().equals(userId)) {
			throw new IllegalStateException("본인의 주문에 대해서만 리뷰를 남길 수 있습니다.");
		}

		if (order.getStatus() != OrderStatus.COMPLETED) {
			throw new IllegalStateException("배송이 완료된 주문만 리뷰 작성이 가능합니다.");
		}

		if (reviewRepository.existsByOrderId(orderId)) {
			throw new IllegalStateException("이미 리뷰를 작성한 주문입니다.");
		}
		// 3. 리뷰 엔티티 생성 및 저장 (빌더 사용)
		Review review = Review.builder()
			.orderId(orderId)
			.userId(userId)
			.store(order.getStore()) // Store 객체 직접 주입
			.rating(request.getRating())
			.content(request.getContent())
			.isHidden(false)
			.build();

		Review savedReview = reviewRepository.save(review);

		// 4. 이미지 처리 및 저장 (이 부분이 핵심 수정 사항입니다!)
		if (images != null && !images.isEmpty()) {
			for (MultipartFile file : images) {
				// (1) LocalImageService 또는 S3ImageService를 통해 파일 업로드 후 URL 획득
				String uploadedUrl = imageService.upload(file, ImageType.REVIEW);

				// (2) 획득한 URL을 ReviewImage 엔티티로 만들어 DB 저장
				ReviewImage image = ReviewImage.builder()
					.review(savedReview)
					.imageUrl(uploadedUrl)
					.build();
				reviewImageRepository.save(image);
			}
		}
		// 5. 가게 평점 업데이트 (Store 엔티티의 비즈니스 메서드 호출)
		order.getStore().addReviewRating(request.getRating());

		return ReviewResponse.from(savedReview);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReviewResponse> getReviewsByStore(UUID storeId, PageReviewRequest request) {

		// 1. PageReviewRequest의 데이터를 바탕으로 정렬(Sort) 객체 생성
		Sort sort = request.isDesc()
			? Sort.by(request.getSortBy()).descending()
			: Sort.by(request.getSortBy()).ascending();

		// 2. Pageable 객체 생성 (페이지 번호, 사이즈, 정렬 조건 포함)
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		// 3. Repository 호출 및 엔티티를 DTO로 변환하여 반환
		return reviewRepository.findAllByStoreIdWithImages(storeId, pageable)
			.map(review -> ReviewResponse.from(review));
	}

	@Override
	@Transactional(readOnly = true)
	public ReviewResponse getReview(UUID reviewId) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
		return ReviewResponse.from(review);
	}

	@Override
	public ReviewResponse updateReview(UUID reviewId, UpdateReviewRequest request) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("수정할 수 없는 리뷰입니다."));

		review.update(request.getRating(), request.getContent());
		return ReviewResponse.from(review);
	}

	@Override
	public void deleteReview(UUID reviewId, UUID userId) {
		Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
			.orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

		// 1. 가게 평점 차감 (삭제 시 기존 평점만큼 빼줘야 함)
		review.getStore().removeReviewRating(review.getRating());

		// 2. 소프트 삭제 처리
		review.delete(userId);
	}
}