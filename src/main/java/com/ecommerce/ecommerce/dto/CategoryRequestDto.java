package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDto(
        @NotBlank(message = "Category name cannot be blank")
        String name,
        Long parentId
) {}
