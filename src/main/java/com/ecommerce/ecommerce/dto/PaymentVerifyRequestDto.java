package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentVerifyRequestDto(
        @NotNull
        Long paymentId,
        @NotBlank
        String transactionId,
        boolean status) {
}
