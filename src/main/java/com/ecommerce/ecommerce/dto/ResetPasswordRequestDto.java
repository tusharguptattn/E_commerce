package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequestDto(
        @NotBlank(message = "New password cannot be blank")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%!]).{8,15}$",
                message = "Password must be 8-15 chars with upper, lower, digit & special char"
        )
        String newPassword,
        @NotBlank(message = "Confirm password cannot be blank")
        String confirmPassword


) {
}
