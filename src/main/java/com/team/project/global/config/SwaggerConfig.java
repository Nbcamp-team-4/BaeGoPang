package com.team.project.global.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	@Bean
	@Profile("prod")
	public OpenAPI prodOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("BaeGoPang API")
						.description("배고팡 백엔드 API 문서")
						.version("v1.0.0"))
				.servers(List.of(
						new Server().url("https://baegopang.kro.kr").description("Production server")
				));
	}

	@Bean
	@Profile("!prod")
	public OpenAPI localOpenApi() {
		return new OpenAPI()
				.info(new Info()
						.title("BaeGoPang API")
						.description("배고팡 백엔드 API 문서")
						.version("v1.0.0"));
	}

	@Bean
	public GroupedOpenApi cartApi() {
		return GroupedOpenApi.builder()
				.group("cart")
				.pathsToMatch("/api/carts/**")
				.build();
	}

	@Bean
	public GroupedOpenApi categoryApi() {
		return GroupedOpenApi.builder()
				.group("category")
				.pathsToMatch("/api/categories/**")
				.build();
	}

	@Bean
	public GroupedOpenApi orderApi() {
		return GroupedOpenApi.builder()
				.group("order")
				.pathsToMatch(
						"/api/orders/**",
						"/api/owner/orders/**",
						"/api/admin/orders/**"
				)
				.build();
	}

	@Bean
	public GroupedOpenApi paymentApi() {
		return GroupedOpenApi.builder()
				.group("payment")
				.pathsToMatch("/api/payments/**")
				.build();
	}

	@Bean
	public GroupedOpenApi paymentLogApi() {
		return GroupedOpenApi.builder()
				.group("payentLog")
				.pathsToMatch("/api/payment-logs/**")
				.build();
	}

	@Bean
	public GroupedOpenApi productApi() {
		return GroupedOpenApi.builder()
				.group("product")
				.pathsToMatch("/api/products/**")
				.build();
	}

	@Bean
	public GroupedOpenApi regionApi() {
		return GroupedOpenApi.builder()
				.group("region")
				.pathsToMatch("/api/regions/**")
				.build();
	}

	@Bean
	public GroupedOpenApi reviewApi() {
		return GroupedOpenApi.builder()
				.group("review")
				.pathsToMatch("/api/reviews/**")
				.build();
	}

	@Bean
	public GroupedOpenApi storeApi() {
		return GroupedOpenApi.builder()
				.group("store")
				.pathsToMatch("/api/stores/**")
				.build();
	}

	@Bean
	public GroupedOpenApi userApi() {
		return GroupedOpenApi.builder()
				.group("user")
				.pathsToMatch("/api/users/**")
				.build();
	}

	@Bean
	public GroupedOpenApi aiApi() {
		return GroupedOpenApi.builder()
				.group("ai")
				.pathsToMatch("/api/ai/**")
				.build();
	}

	@Bean
	public GroupedOpenApi imageApi() {
		return GroupedOpenApi.builder()
				.group("image")
				.pathsToMatch("/api/images/**")
				.build();
	}
}