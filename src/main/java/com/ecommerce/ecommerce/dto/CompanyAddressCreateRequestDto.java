package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CompanyAddressCreateRequestDto(
    @NotBlank(message = "Street cannot be blank")
    String street,
    @NotBlank(message = "City cannot be blank")
    String city,
    @NotBlank(message = "State cannot be blank")
    String state,
    @NotBlank(message = "Zipcode cannot be blank")
    String zipcode,
    @NotBlank(message = "Country cannot be blank")
    String country
) {
}
