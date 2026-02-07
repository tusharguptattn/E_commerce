package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDto(
        @NotBlank(message = "Category name cannot be blank")
        @Size(max = 50, message = "Category name cannot exceed 50 characters")

        String name,
        Long parentId
) {}
