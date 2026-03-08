package com.team.project.domain.order.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmOrderPaymentRequest {

    @NotBlank
    private String paymentKey;

    @NotNull
    private Integer amount;
}