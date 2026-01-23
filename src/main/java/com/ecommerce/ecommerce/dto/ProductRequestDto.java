package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDto(
        @NotBlank(message = "Product name cannot be blank")
        String name,
        @NotBlank(message = "Brand cannot be blank")
        String brand,
        @NotNull(message = "Price cannot be null")
        Long catgegoryId,

        String description,
        boolean isCancellabele,
        boolean isReturnable) {
}
