package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
        @NotBlank
        String street,
        @NotBlank
        String city,
        @NotBlank
        String state,
        @NotBlank
        String zipcode,
        @NotBlank
        String country
) {}


