package com.team.project.domain.payment.infrastructure.toss;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.team.project.domain.payment.infrastructure.toss.dto.TossCancelRequest;
import com.team.project.domain.payment.infrastructure.toss.dto.TossCancelResponse;
import com.team.project.domain.payment.infrastructure.toss.dto.TossConfirmRequest;
import com.team.project.domain.payment.infrastructure.toss.dto.TossConfirmResponse;
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

	public TossPaymentClient(TossPaymentsProperties tossPaymentsProperties) {
		this.tossPaymentsProperties = tossPaymentsProperties;
		this.restClient = RestClient.builder()
			.baseUrl(tossPaymentsProperties.baseUrl())
			.build();

	}

	public TossConfirmResponse confirm(String paymentKey, String orderId, Integer amount) {
		System.out.println("TossPaymentClient.confirm");
		System.out.println("paymentKey = " + paymentKey);
		System.out.println("orderId = " + orderId);
		System.out.println("amount = " + amount);
		TossConfirmRequest request = new TossConfirmRequest(paymentKey, orderId, amount);

		return restClient.post()
			.uri("/v1/payments/confirm")
			.header(HttpHeaders.AUTHORIZATION, createAuthHeader())
			.contentType(MediaType.APPLICATION_JSON)
			.body(request)
			.retrieve()
			.body(TossConfirmResponse.class);
	}

	public TossCancelResponse cancel(String paymentKey, String reason) {

		TossCancelRequest request = new TossCancelRequest(reason);

		return restClient.post()
			.uri("/v1/payments/{paymentKey}/cancel", paymentKey)
			.header(HttpHeaders.AUTHORIZATION, createAuthHeader())
			.contentType(MediaType.APPLICATION_JSON)
			.body(request)
			.retrieve()
			.body(TossCancelResponse.class);

	}

	private String createAuthHeader() {
		String encoded = Base64.getEncoder()
			.encodeToString((tossPaymentsProperties.secretKey() + ":").getBytes(StandardCharsets.UTF_8));
		return "Basic " + encoded;
	}
}
