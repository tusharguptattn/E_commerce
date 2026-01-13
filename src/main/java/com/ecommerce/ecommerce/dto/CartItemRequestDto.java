package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Min;

public record CartItemRequestDto(
        Long productId,
        @Min(1)
        int quantity) {
}
