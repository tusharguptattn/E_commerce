package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(

        @NotBlank
        @Email(message = "Invalid email format")
        String email,

        @NotBlank
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid phone number"
        )
        String phoneNumber,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,15}$",
                message = "Password must be 8-15 chars with upper, lower, digit & special char"
        )
        String password,

        @NotBlank
        String confirmPassword,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        String middleName
) {}
