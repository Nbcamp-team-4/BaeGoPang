package com.team.project.domain.payment.infrastructure.gateway.toss;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.project.domain.payment.infrastructure.exception.PgProviderBaseException;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossCancelRequest;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossCancelResponse;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossConfirmRequest;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossConfirmResponse;
import com.team.project.domain.payment.infrastructure.gateway.toss.dto.TossErrorResponse;
import com.team.project.global.config.TossPaymentsProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * 토스 결제 관련 HTTP 호출
 */
@Component
@Slf4j
public class TossPaymentClient {

	private final TossPaymentsProperties tossPaymentsProperties;
	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	public TossPaymentClient(TossPaymentsProperties tossPaymentsProperties, ObjectMapper objectMapper) {
		this.tossPaymentsProperties = tossPaymentsProperties;
		this.restClient = RestClient.builder()
			.baseUrl(tossPaymentsProperties.baseUrl())
			.build();
		this.objectMapper = objectMapper;
	}

	/**
	 *  TOSS 결제 승인 HTTP 호출
	 */
	public TossConfirmResponse confirm(String paymentKey, String orderId, Integer amount) {
		try {
			TossConfirmRequest request = new TossConfirmRequest(paymentKey, orderId, amount);

			return restClient.post()
				.uri("/v1/payments/confirm")
				.header(HttpHeaders.AUTHORIZATION, createAuthHeader())
				.contentType(MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()

				.body(TossConfirmResponse.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {

			throw parseTossError(e);
		}
	}

	/**
	 *  TOSS 결제 취소 HTTP 호출
	 */
	public TossCancelResponse cancel(String paymentKey, String reason) {
		try {
			TossCancelRequest request = new TossCancelRequest(reason);

			return restClient.post()
				.uri("/v1/payments/{paymentKey}/cancel", paymentKey)
				.header(HttpHeaders.AUTHORIZATION, createAuthHeader())
				.contentType(MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()
				.body(TossCancelResponse.class);

		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw parseTossError(e);
		}

	}

	private PgProviderBaseException parseTossError(RestClientResponseException e) {
		try {
			String body = e.getResponseBodyAsString();
			TossErrorResponse error = objectMapper.readValue(body, TossErrorResponse.class);

			return new PgProviderBaseException(
				error.getCode(),
				error.getMessage()
			);
		} catch (Exception parseException) {
			log.error("Toss error parse failed. body={}", e.getResponseBodyAsString(), parseException);
			return new PgProviderBaseException(
				"TOSS_HTTP_ERROR",
				e.getMessage()
			);
		}
	}

	private String createAuthHeader() {
		String encoded = Base64.getEncoder()
			.encodeToString((tossPaymentsProperties.secretKey() + ":").getBytes(StandardCharsets.UTF_8));
		return "Basic " + encoded;
	}
}
