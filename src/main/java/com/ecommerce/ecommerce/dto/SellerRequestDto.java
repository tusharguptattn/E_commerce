package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SellerRequestDto(
        @NotBlank
        @Email(message = "Invalid email format")
        String email,

        @NotBlank
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Invalid phone number"
        )
        String companyContactNumber,

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

        String middleName,

        @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"
                ,message = "GSTIN number is invalid")
        String gstinNumber,

        String companyName,

        String companyAddress


) {
}
