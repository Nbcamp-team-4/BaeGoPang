package com.team.project.domain.store.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

	private final EntityManager em;

	// 1. 상세 조회 (Fetch Join)
	@Override
	public Optional<Store> findDetailById(UUID storeId) {
		return em.createQuery("""
                select distinct s
                from Store s
                join fetch s.region r
                where s.id = :storeId
                  and s.deletedAt is null
            """, Store.class)
			.setParameter("storeId", storeId)
			.getResultStream()
			.findFirst();
	}

	// 2. 점주용 목록 조회 (메서드 명 통일)
	// 명세서에 user_id라고 되어 있으니, 엔티티 필드가 user라면 s.user.id로 비교해야 합니다.
	@Override
	public List<Store> findAllByUserIdWithDetails(UUID userId) {
		return em.createQuery("""
                select s
                from Store s
                join fetch s.region r
                where s.user.id = :userId
                  and s.deletedAt is null
                order by s.createdAt desc
            """, Store.class)
			.setParameter("userId", userId)
			.getResultList();
	}

	// 3. 관리자용 상태별 조회 (INACTIVE 등)
	@Override
	public List<Store> findAllByStatus(StoreStatus status) {
		return em.createQuery("""
                select s
                from Store s
                join fetch s.region r
                where s.status = :status
                  and s.deletedAt is null
                order by s.createdAt desc
            """, Store.class)
			.setParameter("status", status)
			.getResultList();
	}

	// 4. 사용자용 지역별 검색 (명세서 기준 OPEN 상태만)
	@Override
	public List<Store> findByRegionAndCategory(UUID regionId, UUID categoryId) {
		return em.createQuery("""
                select s
                from Store s
                join fetch s.region r
                where s.region.id = :regionId
                  and s.status = 'OPEN'
                  and s.deletedAt is null
                order by s.createdAt desc
            """, Store.class)
			.setParameter("regionId", regionId)
			.getResultList();
	}
}