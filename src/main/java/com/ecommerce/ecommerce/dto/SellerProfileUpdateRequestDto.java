package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record SellerProfileUpdateRequestDto(
    @NotBlank(message = "first name can not be null")
    String firstName,
    @NotBlank(message = "lastname can not be blank")
    String lastName,
    @NotBlank(message = "companyContact can not be blank")
    String companyContact,
    @NotBlank(message = "companyName can not be blank")
    String companyName,
    @NotBlank(message = "gstNum can not be blank")
    String gstNum,
    @NotBlank(message = "email can not be blank")
    String email) {

}
