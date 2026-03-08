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

	@Override
	public Optional<Store> findDetailById(UUID storeId) {
		return em.createQuery("""
                select distinct s from Store s
                join fetch s.region r
                where s.id = :storeId and s.deletedAt is null
            """, Store.class)
			.setParameter("storeId", storeId)
			.getResultStream().findFirst();
	}

	@Override
	public List<Store> findByUser_IdAndDeletedAtIsNull(UUID userId) {
		return em.createQuery("""
                select s from Store s
                join fetch s.region r
                where s.user.id = :userId and s.deletedAt is null
                order by s.createdAt desc
            """, Store.class)
			.setParameter("userId", userId)
			.getResultList();
	}

	@Override
	public List<Store> findAllWithFilters(StoreStatus status, UUID regionId, UUID userId) {
		// JPQL로 동적 필터 처리 (단순화를 위해 null 체크 로직 포함)
		return em.createQuery("""
                select s from Store s
                where (:status is null or s.status = :status)
                  and (:regionId is null or s.region.id = :regionId)
                  and (:userId is null or s.user.id = :userId)
                  and s.deletedAt is null
                order by s.createdAt desc
            """, Store.class)
			.setParameter("status", status)
			.setParameter("regionId", regionId)
			.setParameter("userId", userId)
			.getResultList();
	}

	@Override
	public List<Store> findNearbyStores(double longitude, double latitude, double distanceInMeters, UUID categoryId) {
		String sql = """
        SELECT s.*
        FROM p_store s
        WHERE s.status = 'OPEN'
          AND s.deleted_at IS NULL
          AND ST_DWithin(
              s.location::geography,
              ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
              :distance
          )
          AND (
              :categoryId::uuid IS NULL
              OR EXISTS (
                  SELECT 1
                  FROM p_store_category sc
                  WHERE sc.store_id = s.id
                    AND sc.category_id = :categoryId
              )
          )
        ORDER BY ST_Distance(
            s.location::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
        ) ASC
    """;

		return em.createNativeQuery(sql, Store.class)
			.setParameter("lng", longitude)
			.setParameter("lat", latitude)
			.setParameter("distance", distanceInMeters)
			.setParameter("categoryId", categoryId)
			.getResultList();
	}
}