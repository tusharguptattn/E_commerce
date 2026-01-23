package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "Invalid email format")
        String email,
        @NotBlank
        @Size(min = 8,max = 20,message = "Password must be between 8 and 20 characters")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,15}$",
                message = "Password must contain uppercase, lowercase, digit and special character"
        )
        String password) {
}
