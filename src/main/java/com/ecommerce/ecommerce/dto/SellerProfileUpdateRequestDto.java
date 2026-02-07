package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SellerProfileUpdateRequestDto(
    @NotBlank(message = "first name can not be null")
    String firstName,
    @NotBlank(message = "lastname can not be blank")
    String lastName,
    @NotBlank(message = "companyContact can not be blank")
    @Pattern(regexp = "^[6-9]\\d{9}$",message = "contact number is invalid")
    String companyContact,
    @NotBlank(message = "companyName can not be blank")
    String companyName,
    @NotBlank(message = "gstNum can not be blank")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",message = "gst number is invalid")
    String gstNum,
    @NotBlank(message = "email can not be blank")
    @Email(message = "invalid email format")
    String email) {

}
