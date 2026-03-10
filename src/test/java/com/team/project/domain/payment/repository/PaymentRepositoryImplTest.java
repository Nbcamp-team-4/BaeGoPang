package com.team.project.domain.payment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;

@SpringBootTest
class PaymentRepositoryImplTest {

	@Autowired
	private PaymentRepository paymentRepository;

	@Test
	@Transactional
	void getPayments() {

	}

}