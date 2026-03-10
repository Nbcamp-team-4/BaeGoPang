package com.team.project.domain.payment.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.team.project.domain.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentSuccessEventListener {

	private final CartService cartService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(PaymentSuccessEvent event) {
		try {
			cartService.clearCart(event.userId());
		} catch (Exception e) {
			log.error(
				"결제 성공 후 장바구니 삭제 실패. userId={}",
				event.userId()
			);
		}
	}
}