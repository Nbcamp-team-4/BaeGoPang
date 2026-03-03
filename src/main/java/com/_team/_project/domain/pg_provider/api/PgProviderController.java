package com._team._project.domain.pg_provider.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._team._project.domain.pg_provider.api.request.CreatePgProviderRequest;
import com._team._project.domain.pg_provider.api.request.GetPgProvidersRequest;
import com._team._project.domain.pg_provider.api.request.UpdatePgProviderRequest;
import com._team._project.domain.pg_provider.api.response.CreatePgProviderResponse;
import com._team._project.domain.pg_provider.api.response.GetPgProviderResponse;
import com._team._project.domain.pg_provider.api.response.GetPgProvidersResponse;
import com._team._project.domain.pg_provider.api.response.UpdatePgProviderResponse;
import com._team._project.domain.pg_provider.service.PgProviderService;
import com._team._project.global.common.dto.BaseResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pg-providers")
@Slf4j
@RequiredArgsConstructor
public class PgProviderController {

	private final PgProviderService pgProviderService;

	@PostMapping
	public ResponseEntity<?> createPgProvider(@RequestBody @Valid CreatePgProviderRequest request) {

		CreatePgProviderResponse response = pgProviderService.createPgProvider(request);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@GetMapping("/{providerId}")
	public ResponseEntity<?> getPgProvider(@PathVariable("providerId") UUID providerId) {

		GetPgProviderResponse response = pgProviderService.getPgProvider(providerId);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@DeleteMapping("/{providerId}")
	public ResponseEntity<?> deletePgProvider(@PathVariable("providerId") UUID providerId) {

		pgProviderService.deletePgProvider(providerId);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(null)
		);
	}

	@PatchMapping("/{providerId}")
	public ResponseEntity<?> updatePgProvider(@PathVariable("providerId") UUID providerId,
		@RequestBody @Valid UpdatePgProviderRequest request) {

		UpdatePgProviderResponse response = pgProviderService.updatePgProvider(providerId, request);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

	@GetMapping
	public ResponseEntity<?> getPgProviders(@ModelAttribute GetPgProvidersRequest request) {

		GetPgProvidersResponse response = pgProviderService.getPgProviders(request);

		return ResponseEntity.ok().body(
			BaseResponse.ofSuccess(response)
		);
	}

}
