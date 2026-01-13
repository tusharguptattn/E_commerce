package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.*;

public record UserRequestDto(
        @NotBlank
        @Size(max=50, message = "Name must not exceed 50 characters")
        String name,
        @Email(message = "Invalid email format")
        String email,
        @NotBlank
        @Size(min = 8,max = 20,message = "Password must be between 8 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
                message = "Password must contain uppercase, lowercase, digit and special character"
        )
        String password,


        @Size(max = 10, message = "Name must not exceed 10 characters")
        @NotNull
        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
        String phoneNumber
) {
}
