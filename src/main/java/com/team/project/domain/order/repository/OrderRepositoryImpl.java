package com.team.project.domain.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.team.project.domain.order.entity.Order;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.team.project.domain.order.model.vo.OrderStatus;
import com.team.project.global.common.dto.BaseRangeRequest;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

	private final EntityManager em;

	@Override
	public Optional<Order> findDetailById(UUID orderId) {
		// 주문 상세 조회
		// - 주문 + 유저/가게 + 배송지 + 주문상품 + 옵션까지 한 번에 가져오기(fetch join)
		// - 컬렉션(items/options) fetch join은 결과 row가 늘어날 수 있어서 distinct로 중복 제거
		return em.createQuery("""
				select distinct o
				from Order o
				join fetch o.user u
				join fetch o.store s
				left join fetch o.deliveryAddress a
				left join fetch o.items i
				left join fetch i.options io
				join fetch i.product p
				where o.id = :orderId
			""", Order.class)
				.setParameter("orderId", orderId)
				.getResultStream()
				.findFirst();
	}

	@Override
	public Optional<Order> findDetailByIdAndUserId(UUID orderId, UUID userId) {
		String jpql = """
        select distinct o
        from Order o
        join fetch o.user u
        join fetch o.store s
        left join fetch o.items i
        left join fetch i.product p
        where o.id = :orderId
          and u.id = :userId
    """;

		return em.createQuery(jpql, Order.class)
				.setParameter("orderId", orderId)
				.setParameter("userId", userId)
				.getResultStream()
				.findFirst();
	}

	@Override
	public List<Order> findAllByUserIdOrderByOrderDateDesc(UUID userId) {
		// 고객용 주문 목록 조회
		// - 목록 조회는 가볍게 가져오는 게 좋아서 "주문상품/옵션"은 fetch join 하지 않음
		// - 대신 화면에 가게 정보가 필요할 수 있어서 store만 fetch join
		return em.createQuery("""
				select o
				from Order o
				join fetch o.store s
				where o.user.id = :userId
				order by o.orderDate desc
			""", Order.class)
				.setParameter("userId", userId)
				.getResultList();
	}

	@Override
	public List<Order> findAllByStoreIdOrderByOrderDateDesc(UUID storeId) {
		// 가게(매니저)용 주문 목록 조회
		// - 특정 가게(storeId)에 들어온 주문들을 최신순으로 조회
		// - 목록 조회는 가볍게: 주문상품/옵션은 제외
		// - 대신 고객 정보를 보여줄 수 있어서 user는 fetch join
		return em.createQuery("""
				select o
				from Order o
				join fetch o.user u
				where o.store.id = :storeId
				order by o.orderDate desc
			""", Order.class)
				.setParameter("storeId", storeId)
				.getResultList();
	}

	@Override
	public Optional<Order> findDetailByIdAndStoreId(UUID orderId, UUID storeId) {
		String jpql = """
        select distinct o
        from Order o
        join fetch o.user u
        join fetch o.store s
        left join fetch o.items i
        left join fetch i.product p
        where o.id = :orderId
          and s.id = :storeId
    """;

		return em.createQuery(jpql, Order.class)
				.setParameter("orderId", orderId)
				.setParameter("storeId", storeId)
				.getResultStream()
				.findFirst();
	}

	@Override
	public Page<Order> searchMyOrders(
			UUID userId,
			OrderStatus status,
			BaseRangeRequest<LocalDateTime> rangeCreatedAt,
			Pageable pageable
	) {
		LocalDateTime minCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMin() : null;
		LocalDateTime maxCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMax() : null;

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// content query
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> order = cq.from(Order.class);
		order.fetch("store");

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(order.get("user").get("id"), userId));

		if (status != null) {
			predicates.add(cb.equal(order.get("status"), status));
		}

		if (minCreatedAt != null) {
			predicates.add(cb.greaterThanOrEqualTo(order.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			predicates.add(cb.lessThanOrEqualTo(order.get("createdAt"), maxCreatedAt));
		}

		cq.select(order).distinct(true);
		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(order.get("createdAt")));

		TypedQuery<Order> query = em.createQuery(cq);
		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Order> content = query.getResultList();

		// count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Order> countRoot = countQuery.from(Order.class);

		List<Predicate> countPredicates = new ArrayList<>();
		countPredicates.add(cb.equal(countRoot.get("user").get("id"), userId));

		if (status != null) {
			countPredicates.add(cb.equal(countRoot.get("status"), status));
		}

		if (minCreatedAt != null) {
			countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("createdAt"), maxCreatedAt));
		}

		countQuery.select(cb.count(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Order> searchStoreOrders(
			UUID storeId,
			OrderStatus status,
			BaseRangeRequest<LocalDateTime> rangeCreatedAt,
			Pageable pageable
	) {
		LocalDateTime minCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMin() : null;
		LocalDateTime maxCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMax() : null;

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// content query
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> order = cq.from(Order.class);
		order.fetch("user");
		order.fetch("store");

		List<Predicate> predicates = new ArrayList<>();
		predicates.add(cb.equal(order.get("store").get("id"), storeId));

		if (status != null) {
			predicates.add(cb.equal(order.get("status"), status));
		}

		if (minCreatedAt != null) {
			predicates.add(cb.greaterThanOrEqualTo(order.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			predicates.add(cb.lessThanOrEqualTo(order.get("createdAt"), maxCreatedAt));
		}

		cq.select(order).distinct(true);
		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(order.get("createdAt")));

		TypedQuery<Order> query = em.createQuery(cq);
		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Order> content = query.getResultList();

		// count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Order> countRoot = countQuery.from(Order.class);

		List<Predicate> countPredicates = new ArrayList<>();
		countPredicates.add(cb.equal(countRoot.get("store").get("id"), storeId));

		if (status != null) {
			countPredicates.add(cb.equal(countRoot.get("status"), status));
		}

		if (minCreatedAt != null) {
			countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("createdAt"), maxCreatedAt));
		}

		countQuery.select(cb.count(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Order> searchAdminOrders(
			UUID storeId,
			UUID userId,
			OrderStatus status,
			BaseRangeRequest<LocalDateTime> rangeCreatedAt,
			Pageable pageable
	) {
		LocalDateTime minCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMin() : null;
		LocalDateTime maxCreatedAt = rangeCreatedAt != null ? rangeCreatedAt.getMax() : null;

		CriteriaBuilder cb = em.getCriteriaBuilder();

		// content query
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> order = cq.from(Order.class);
		order.fetch("user");
		order.fetch("store");

		List<Predicate> predicates = new ArrayList<>();

		if (storeId != null) {
			predicates.add(cb.equal(order.get("store").get("id"), storeId));
		}

		if (userId != null) {
			predicates.add(cb.equal(order.get("user").get("id"), userId));
		}

		if (status != null) {
			predicates.add(cb.equal(order.get("status"), status));
		}

		if (minCreatedAt != null) {
			predicates.add(cb.greaterThanOrEqualTo(order.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			predicates.add(cb.lessThanOrEqualTo(order.get("createdAt"), maxCreatedAt));
		}

		cq.select(order).distinct(true);
		cq.where(predicates.toArray(new Predicate[0]));
		cq.orderBy(cb.desc(order.get("createdAt")));

		TypedQuery<Order> query = em.createQuery(cq);
		query.setFirstResult((int)pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		List<Order> content = query.getResultList();

		// count query
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		Root<Order> countRoot = countQuery.from(Order.class);

		List<Predicate> countPredicates = new ArrayList<>();

		if (storeId != null) {
			countPredicates.add(cb.equal(countRoot.get("store").get("id"), storeId));
		}

		if (userId != null) {
			countPredicates.add(cb.equal(countRoot.get("user").get("id"), userId));
		}

		if (status != null) {
			countPredicates.add(cb.equal(countRoot.get("status"), status));
		}

		if (minCreatedAt != null) {
			countPredicates.add(cb.greaterThanOrEqualTo(countRoot.get("createdAt"), minCreatedAt));
		}

		if (maxCreatedAt != null) {
			countPredicates.add(cb.lessThanOrEqualTo(countRoot.get("createdAt"), maxCreatedAt));
		}

		countQuery.select(cb.count(countRoot));
		countQuery.where(countPredicates.toArray(new Predicate[0]));

		Long total = em.createQuery(countQuery).getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}


}