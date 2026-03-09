package com.team.project.domain.review.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team.project.domain.order.entity.Order;
import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.domain.order.repository.OrderRepository;
import com.team.project.domain.review.api.request.CreateReviewRequest;
import com.team.project.domain.review.api.response.ReviewResponse;
import com.team.project.domain.review.api.response.UpdateReviewRequest;
import com.team.project.domain.review.entity.Review;
import com.team.project.domain.review.entity.ReviewImage;
import com.team.project.domain.review.repository.ReviewImageRepository;
import com.team.project.domain.review.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final OrderRepository orderRepository;

	@Override
	public ReviewResponse createReview(UUID orderId, UUID userId, CreateReviewRequest request) {
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

		// 4. 이미지 저장
		if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
			for (String url : request.getImageUrls()) {
				ReviewImage image = ReviewImage.builder()
					.review(savedReview)
					.imageUrl(url)
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
	public List<ReviewResponse> getReviewsByStore(UUID storeId) {
		// Repository에 findAllByStoreIdAndDeletedAtIsNull 메서드가 있어야 합니다.
		List<Review> reviews = reviewRepository.findAllByStoreIdAndDeletedAtIsNull(storeId);
		return reviews.stream()
			.map(ReviewResponse::from)
			.collect(Collectors.toList());
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