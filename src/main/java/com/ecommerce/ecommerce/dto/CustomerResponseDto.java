package com.ecommerce.ecommerce.dto;

public record CustomerResponseDto(
        Long id,
        String fullName,
        String email,
        boolean isActive
) {
}
