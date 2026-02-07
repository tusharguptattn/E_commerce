package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequestDto(
        @NotBlank(message = "Product name cannot be blank")
        @Size(max=50, message = "Product name cannot exceed 50 characters")
        String name,
        @NotBlank(message = "Brand cannot be blank")
        String brand,
        @NotNull(message = "Price cannot be null")
        Long catgegoryId,
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,
        boolean isCancellabele,
        boolean isReturnable) {
}
