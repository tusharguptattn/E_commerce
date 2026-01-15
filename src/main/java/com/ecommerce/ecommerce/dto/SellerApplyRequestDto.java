package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SellerApplyRequestDto(

        @NotBlank(message = "Business name is required")
        @Size(min = 3, max = 100)
        String businessName,

        @NotBlank(message = "GST number is required")
        @Pattern(
                regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
                message = "Invalid GST number"
        )
        String gstNumber,

        @NotBlank(message = "Bank account number is required")
        String bankAccountNumber,

        @NotBlank(message = "IFSC code is required")
        @Pattern(
                regexp = "^[A-Z]{4}0[A-Z0-9]{6}$",
                message = "Invalid IFSC code"
        )
        String ifscCode

) {}
