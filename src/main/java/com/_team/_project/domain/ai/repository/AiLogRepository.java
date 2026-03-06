package com._team._project.domain.ai.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._team._project.domain.ai.entity.AiLog;

/**
 * p_ai_log 테이블에 접근하기 위한 전용 레포지토리입니다.
 */
@Repository
public interface AiLogRepository extends JpaRepository<AiLog, UUID> {
	// JpaRepository를 상속받으면 save(), findById() 등을 자동으로 사용할 수 있습니다.
}