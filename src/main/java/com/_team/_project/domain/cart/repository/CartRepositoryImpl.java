package com._team._project.domain.cart.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com._team._project.domain.cart.entity.Cart;
import com._team._project.domain.cart.model.vo.CartStatus;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

    private final EntityManager em;

    @Override
    public Optional<Cart> findActiveCartDetailByUserId(UUID userId) {
        // 유저의 ACTIVE 장바구니를 "상세"로 조회
        // - cart + store + items + itemOptions 까지 한번에 가져오기(fetch join)
        // - 컬렉션(items/options) fetch join은 row가 늘어날 수 있어서 distinct로 중복 제거
		return em.createQuery("""
			select distinct c
			from Cart c
			join fetch c.store s
			left join fetch c.items ci
			left join fetch ci.product p
			where c.user.id = :userId
			  and c.status = :status
			  and c.deletedAt is null
		""", Cart.class)
		.setParameter("userId", userId)
		.setParameter("status", CartStatus.ACTIVE)
		.getResultStream()
		.findFirst();
    }

	@Override
	public Optional<Cart> findCartDetailById(UUID cartId) {
		return em.createQuery("""
        select distinct c
        from Cart c
        join fetch c.user u
        join fetch c.store s
        left join fetch c.items ci
        left join fetch ci.product p
        where c.id = :cartId
          and c.deletedAt is null
    """, Cart.class)
				.setParameter("cartId", cartId)
				.getResultStream()
				.findFirst();
	}

    @Override
    public Optional<Cart> findActiveCartByUserId(UUID userId) {
        // ✅ 유저의 ACTIVE 장바구니를 "가볍게" 조회
        // - 다른 가게 상품 담기 정책 판단(현재 cart의 storeId 비교) 등에 사용
        // - items/options는 가져오지 않아서 성능 부담이 적음
        return em.createQuery("""
				select c
				from Cart c
				where c.user.id = :userId
				  and c.status = :status
				  and c.deletedAt is null
			""", Cart.class)
                .setParameter("userId", userId)
                .setParameter("status", CartStatus.ACTIVE)
                .getResultStream()
                .findFirst();
    }

	@Override
	public void fetchItemOptionsByCartId(UUID cartId) {
		// cart.items는 이미 1번 쿼리에서 fetch됨
		// 여기서는 items.options만 한 번에 땡겨서 영속성 컨텍스트에 로딩시키는 용도
		em.createQuery("""
        select distinct c
        from Cart c
        join fetch c.items ci
        left join fetch ci.options cio
        left join fetch cio.productOption po
        left join fetch cio.productOptionItem poi
        where c.id = :cartId
          and c.deletedAt is null
    """, Cart.class)
				.setParameter("cartId", cartId)
				.getResultList();
	}
}