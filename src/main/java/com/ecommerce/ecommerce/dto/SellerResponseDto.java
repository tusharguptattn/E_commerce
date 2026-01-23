package com.ecommerce.ecommerce.dto;

public record SellerResponseDto(
        Long id,
        String fullName,
        String email,
        boolean isActive,
        String companyName,
        String companyAddress,
        String companyContactNumber) {

}
