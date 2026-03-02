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
        // items / options는 컬렉션이라 fetch join하면 row 증가 -> DISTINCT로 중복 제거
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
        // 목록은 상세처럼 아이템까지 다 fetch하면 무거움 -> 일단 user/store만
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
}