package com.team.project.domain.payment.event;

import java.util.UUID;

public record PaymentSuccessEvent(UUID userId) {
}