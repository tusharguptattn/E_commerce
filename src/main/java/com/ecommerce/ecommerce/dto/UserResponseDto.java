package com.ecommerce.ecommerce.dto;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}
