package com.team.project.domain.store.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom {

	private final EntityManager em;

	@Override
	public Optional<Store> findDetailById(UUID storeId) {
		return em.createQuery("""
				select s from Store s
				join fetch s.region r
				join fetch s.user u
				where s.id = :storeId
				  and s.deletedAt is null
			""", Store.class)
			.setParameter("storeId", storeId)
			.getResultStream()
			.findFirst();
	}

	@Override
	public List<Store> findByUser_IdAndDeletedAtIsNull(UUID userId) {
		return em.createQuery("""
				select s from Store s
				join fetch s.region r
				join fetch s.user u
				where s.user.id = :userId
				  and s.deletedAt is null
				order by s.createdAt desc
			""", Store.class)
			.setParameter("userId", userId)
			.getResultList();
	}

	@Override
	public Page<Store> findAllWithFilters(
		String keyword,
		StoreStatus status,
		UUID regionId,
		UUID userId,
		UUID categoryId,
		Pageable pageable
	) {
		StringBuilder contentJpql = new StringBuilder("""
			select s from Store s
			join fetch s.region r
			join fetch s.user u
			where s.deletedAt is null
		""");

		StringBuilder countJpql = new StringBuilder("""
			select count(s) from Store s
			where s.deletedAt is null
		""");

		Map<String, Object> params = new HashMap<>();

		if (keyword != null && !keyword.isBlank()) {
			contentJpql.append(" and lower(s.name) like lower(concat('%', :keyword, '%'))");
			countJpql.append(" and lower(s.name) like lower(concat('%', :keyword, '%'))");
			params.put("keyword", keyword);
		}

		if (status != null) {
			contentJpql.append(" and s.status = :status");
			countJpql.append(" and s.status = :status");
			params.put("status", status);
		}

		if (regionId != null) {
			contentJpql.append(" and s.region.id = :regionId");
			countJpql.append(" and s.region.id = :regionId");
			params.put("regionId", regionId);
		}

		if (userId != null) {
			contentJpql.append(" and s.user.id = :userId");
			countJpql.append(" and s.user.id = :userId");
			params.put("userId", userId);
		}

		if (categoryId != null) {
			contentJpql.append("""
				 and exists (
				 	select 1
				 	from StoreCategory sc
				 	where sc.store = s
				 	  and sc.category.id = :categoryId
				 	  and sc.deletedAt is null
				 )
				""");

			countJpql.append("""
				 and exists (
				 	select 1
				 	from StoreCategory sc
				 	where sc.store = s
				 	  and sc.category.id = :categoryId
				 	  and sc.deletedAt is null
				 )
				""");

			params.put("categoryId", categoryId);
		}

		contentJpql.append(" order by s.createdAt desc");

		TypedQuery<Store> contentQuery = em.createQuery(contentJpql.toString(), Store.class);
		TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			contentQuery.setParameter(entry.getKey(), entry.getValue());
			countQuery.setParameter(entry.getKey(), entry.getValue());
		}

		contentQuery.setFirstResult((int) pageable.getOffset());
		contentQuery.setMaxResults(pageable.getPageSize());

		List<Store> content = contentQuery.getResultList();
		Long total = countQuery.getSingleResult();

		return new PageImpl<>(content, pageable, total);
	}

	@Override
	public Page<Store> findNearbyStores(
		double longitude,
		double latitude,
		double distanceInMeters,
		UUID categoryId,
		Pageable pageable
	) {
		String idSql = """
			SELECT s.id
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
			  		  AND sc.deleted_at IS NULL
			  	)
			  )
			ORDER BY ST_Distance(
				s.location::geography,
				ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
			) ASC
		""";

		String countSql = """
			SELECT COUNT(*)
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
			  		  AND sc.deleted_at IS NULL
			  	)
			  )
		""";

		Query idQuery = em.createNativeQuery(idSql)
			.setParameter("lng", longitude)
			.setParameter("lat", latitude)
			.setParameter("distance", distanceInMeters)
			.setParameter("categoryId", categoryId);

		idQuery.setFirstResult((int) pageable.getOffset());
		idQuery.setMaxResults(pageable.getPageSize());

		@SuppressWarnings("unchecked")
		List<Object> rawIds = idQuery.getResultList();

		List<UUID> ids = rawIds.stream()
			.map(value -> {
				if (value instanceof UUID uuid) {
					return uuid;
				}
				return UUID.fromString(value.toString());
			})
			.toList();

		if (ids.isEmpty()) {
			Number totalNumber = (Number) em.createNativeQuery(countSql)
				.setParameter("lng", longitude)
				.setParameter("lat", latitude)
				.setParameter("distance", distanceInMeters)
				.setParameter("categoryId", categoryId)
				.getSingleResult();

			return new PageImpl<>(Collections.emptyList(), pageable, totalNumber.longValue());
		}

		List<Store> stores = em.createQuery("""
				select s from Store s
				join fetch s.region r
				join fetch s.user u
				where s.id in :ids
			""", Store.class)
			.setParameter("ids", ids)
			.getResultList();

		Map<UUID, Integer> orderMap = new HashMap<>();
		for (int i = 0; i < ids.size(); i++) {
			orderMap.put(ids.get(i), i);
		}

		List<Store> orderedStores = stores.stream()
			.sorted(Comparator.comparingInt(store -> orderMap.get(store.getId())))
			.toList();

		Number totalNumber = (Number) em.createNativeQuery(countSql)
			.setParameter("lng", longitude)
			.setParameter("lat", latitude)
			.setParameter("distance", distanceInMeters)
			.setParameter("categoryId", categoryId)
			.getSingleResult();

		return new PageImpl<>(orderedStores, pageable, totalNumber.longValue());
	}
}