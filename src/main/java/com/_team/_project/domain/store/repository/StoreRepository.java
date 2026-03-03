package com._team._project.domain.store.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, UUID> {

	List<Store> findAllByDeletedAtIsNull();

}