package com.ecommerce.ecommerce.dto;

public record CustomerViewMyProfileDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String imageUrl,
        boolean isActive) {
}
