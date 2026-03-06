package com._team._project.domain.ai.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com._team._project.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AiLog extends BaseEntity { // BaseEntity에 created_at 등이 있다고 가정

	@Id
	@UuidGenerator // 자동으로 UUID를 생성
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String requestText;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String responseText;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AiTaskType taskType;

}