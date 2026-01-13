package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderRequestDto(
        Long addressId,
        @NotBlank
        String paymentMethod) {
}
