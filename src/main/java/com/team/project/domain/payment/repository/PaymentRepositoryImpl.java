package com.team.project.domain.payment.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.team.project.domain.payment.entity.Payment;
import com.team.project.domain.payment.model.vo.PaymentStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;
	private final EntityManager em;

	/**
	 * 결제 생성
	 */
	@Override
	public Payment createPayment(Payment payment) {
		Payment save = paymentJpaRepository.save(payment);
		return save;
	}

	/**
	 * 결제 ID로 결제 단건 조회
	 */
	@Override
	public Optional<Payment> getPayment(UUID paymentId) {
		return paymentJpaRepository.findById(paymentId);
	}

	/**
	 * 주문 ID로 최신 결제 조회(삭제 포함)
	 * <p>
	 * 주문 ID를 받아서 삭제된 것을 포함하여 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderContainsDeleted(UUID orderId) {
		return paymentJpaRepository
			.findLatestByOrderContainsDeleted(orderId)
			.stream()
			.findFirst();
	}

	/**
	 * 주문 ID, 결제 상태로 최신 결제 조회(삭제 제외)
	 * <p>
	 * 주문 ID와 결제 상태를 받아서 조건에 맞는 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderAndStatus(UUID orderId, PaymentStatus status) {
		return paymentJpaRepository
			.findLatestByOrderAndStatus(orderId, status, PageRequest.of(0, 1))
			.stream()
			.findFirst();
	}

	/**
	 * 주문 ID를 받아서 삭제된 것을 제외하여 가장 최신 결제를 반환합니다.
	 */
	@Override
	public Optional<Payment> getLatestPaymentByOrderId(UUID orderId) {
		return paymentJpaRepository.findLatestByOrder(orderId)
			.stream()
			.findFirst();
	}

	/**
	 * 여러 조건을 받아서 결제 리스트를 조회합니다.
	 */
	@Override
	public Page<Payment> getPayments(PaymentStatus paymentStatus, BaseRangeRequest<Integer> rangeAmount,
		BaseRangeRequest<LocalDateTime> rangePaidAt, UUID orderId, Pageable pageable) {

		Integer minAmount = rangeAmount != null ? rangeAmount.getMin() : null;
		Integer maxAmount = rangeAmount != null ? rangeAmount.getMax() : null;
		LocalDateTime minPaidAt = rangePaidAt != null ? rangePaidAt.getMin() : null;
		LocalDateTime maxPaidAt = rangePaidAt != null ? rangePaidAt.getMax() : null;

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// content query
		CriteriaQuery<Payment> cq = cb.createQuery(Payment.class);
		Root<Payment> payment = cq.from(Payment.class);

		List<Predicate> predicates = new ArrayList<>();

		if (paymentStatus != null) {
			predicates.add(cb.equal(payment.get("status"), paymentStatus));
		}

		if (orderId != null) {
			predicates.add(cb.equal(payment.get("order").get("id"), orderId));
		}

		if (minAmount != null) {
			predicates.add(cb.greaterThanOrEqualTo(payment.get("amount"), minAmount));
		}

		if (maxAmount != null) {
			predicates.add(cb.lessThanOrEqualTo(payment.get("amount"), maxAmount));
		}

		if (minPaidAt != null) {
			predicates.add(cb.greaterThanOrEqualTo(payment.get("paidAt"), minPaidAt));
		}

		if (maxPaidAt != null) {
			predicates.add(cb.lessThanOrEqualTo(payment.get("paidAt"), maxPaidAt));
		}

		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(payment.get("createdAt")));

		TypedQuery<Payment> query = em.createQuery(cq);
		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Payment> content = query.getResultList();

		// count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Payment> countRoot = countQuery.from(Payment.class);

		List<Predicate> countPredicates = new ArrayList<>();

		if (paymentStatus != null) {
			countPredicates.add(cb.equal(countRoot.get("status"), paymentStatus));
		}

		if (orderId != null) {
			countPredicates.add(cb.equal(countRoot.get("order").get("id"), orderId));
		}

		if (minAmount != null) {
			countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("amount"), minAmount));
		}

		if (maxAmount != null) {
			countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("amount"), maxAmount));
		}

		if (minPaidAt != null) {
			countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("paidAt"), minPaidAt));
		}

		if (maxPaidAt != null) {
			countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("paidAt"), maxPaidAt));
		}

		countQuery.select(cb.count(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}
}
