package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        Long id,
        @NotBlank
        String name
) {}
