package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequestDto(
        @NotNull(message = "Product Id can not bt null")
        Long ProductId,
        @NotBlank(message = "Product name cannot be blank")
        @Size(max=50, message = "Product name cannot exceed 50 characters")
        String name,
        @Size(max=255, message = "Description cannot exceed 255 characters")
        String desription,
        boolean isCancellable,
        boolean isReturnable) {
}
