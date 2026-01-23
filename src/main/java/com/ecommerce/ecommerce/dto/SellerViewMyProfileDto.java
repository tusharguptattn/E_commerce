package com.ecommerce.ecommerce.dto;

public record SellerViewMyProfileDto(Long id,
                                     String firstName,
                                     String lastName,
                                     boolean isActive,
                                     String companyContact,
                                     String companyName,
                                     String imageUrl,
                                     String gstNumber,
                                     String city,
                                     String state,
                                     String country,
                                     String zipCode,
                                     String streetAddress) {
}
