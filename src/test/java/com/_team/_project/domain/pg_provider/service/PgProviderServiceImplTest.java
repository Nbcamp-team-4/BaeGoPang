package com._team._project.domain.pg_provider.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class PgProviderServiceImplTest {

	@Autowired
	private PgProviderService pgProviderService;

	@Test
	@Rollback
	void createPgProvider() {
		CreatePgProviderRequest request = new CreatePgProviderRequest();
		request.setCode("TOSS");
		request.setName("토스페이먼츠사");

		CreatePgProviderResponse response = pgProviderService.createPgProvider(
			request
		);
		assertThat(response.getCode()).isEqualTo(request.getCode());
		assertThat(response.getName()).isEqualTo(request.getName());
	}
}