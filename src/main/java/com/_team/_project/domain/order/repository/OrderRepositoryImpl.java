package com._team._project.domain.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com._team._project.domain.order.entity.Order;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

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
		// 고객용 주문 상세 조회(권한 체크)
		// - orderId + userId 조건으로 "내 주문"인지 확인하면서 상세 조회
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
				  and u.id = :userId
			""", Order.class)
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
		// 가게(매니저)용 주문 상세 조회(소유 확인)
		// - orderId + storeId 조건으로 "우리 가게 주문"인지 확인하면서 상세 조회
		// - 주문상품/옵션까지 모두 fetch join 해서 상세 화면에 필요한 데이터 한 번에 조회
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
				  and s.id = :storeId
			""", Order.class)
				.setParameter("orderId", orderId)
				.setParameter("storeId", storeId)
				.getResultStream()
				.findFirst();
	}
}