package com._team._project.domain.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com._team._project.domain.user.entity.UserAddress;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    // (선택) 주문 생성 시 "내 배송지"인지 검증하고 싶으면 이 메서드를 쓰면 됨
    Optional<UserAddress> findByIdAndUserId(UUID id, UUID userId);
}