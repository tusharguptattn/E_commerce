package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotNull;

public record ProductUpdateRequestDto(
        @NotNull(message = "Product Id can not bt null")
        Long ProductId,
        String name,
        String desription,
        boolean isCancellable,
        boolean isReturnable) {
}
