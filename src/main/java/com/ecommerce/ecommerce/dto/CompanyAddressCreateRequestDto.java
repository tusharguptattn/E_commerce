package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CompanyAddressCreateRequestDto(
    @NotBlank(message = "Street cannot be blank")
    @Size(max = 50, message = "Street cannot exceed 255 characters")
    String street,
    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City cannot exceed 255 characters")
    String city,
    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State cannot exceed 255 characters")
    String state,
    @NotBlank(message = "Zipcode cannot be blank")
    @Pattern(regexp = "^[1-9][0-9]{5}$",message = "Zipcode must be a 6-digit number starting with 1-9")
    String zipcode,
    @NotBlank(message = "Country cannot be blank")
    String country
) {
}
