package com.team.project.domain.product.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "p_product_ai_log")
public class ProductAiLog {

	@Id
	private UUID id;

	@Column(name = "product_id", nullable = false)
	private UUID productId;

	@Column(name = "prompt", nullable = false, columnDefinition = "text")
	private String prompt;

	@Column(name = "generated_text", nullable = false, columnDefinition = "text")
	private String generatedText;

	@Column(name = "model_name", nullable = false, length = 50)
	private String modelName;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by")
	private UUID createdBy;

	public static ProductAiLog create(UUID productId, String prompt, String generatedText, String modelName, UUID createdBy) {
		return ProductAiLog.builder()
			.id(UUID.randomUUID())
			.productId(productId)
			.prompt(prompt)
			.generatedText(generatedText)
			.modelName(modelName)
			.createdAt(LocalDateTime.now())
			.createdBy(createdBy)
			.build();
	}
}