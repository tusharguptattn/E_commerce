package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequestDto(
        @NotNull(message = "Category ID cannot be null")
        Long id,
        @NotBlank(message = "Category name cannot be blank")
        String name) {
}
