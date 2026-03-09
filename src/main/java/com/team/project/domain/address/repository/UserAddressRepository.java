package com.team.project.domain.address.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.project.domain.user.entity.User;
import com.team.project.domain.address.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

	// (선택) 주문 생성 시 "내 배송지"인지 검증하고 싶으면 이 메서드를 쓰면 됨
	Optional<UserAddress> findByIdAndUserId(UUID id, UUID userId);

	@Query("select ua from UserAddress ua where ua.isDefault = false and ua.user = :user")
	List<UserAddress> findDefaultAddressByUser(@Param("user") User user);

	Page<UserAddress> findAllByUserId(UUID userId, Pageable pageable);

	Optional<UserAddress> findByUserId(UUID userId);

	boolean existsByIdAndUserIdAndDeletedAtIsNull(UUID id, UUID userId);
}