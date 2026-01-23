package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.UserEntity;

public record UserResponseDto(
        Long id,
        String firstName,
        String middleName,
        String  lastName,
        String email,
        boolean isActive
) {

    public static UserResponseDto mapEntity(UserEntity user) {
        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive()
        );
    }
}
