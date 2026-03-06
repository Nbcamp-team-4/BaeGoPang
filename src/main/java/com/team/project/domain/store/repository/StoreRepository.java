
package com.team.project.domain.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.project.domain.store.entity.Store;
import com.team.project.domain.store.model.vo.StoreStatus;

public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {

	// 단순 조회는 JpaRepository가 알아서 쿼리를 만듭니다.
	List<Store> findAllByRegionIdAndStatus(UUID regionId, StoreStatus status);
}