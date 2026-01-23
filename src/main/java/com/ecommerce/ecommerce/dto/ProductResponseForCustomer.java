package com.ecommerce.ecommerce.dto;

import java.util.List;

public record ProductResponseForCustomer(String name,
                                         String brand,
                                         String description,
                                         ChildCategoryResponseDto category,
                                         List<ProductVariationResponseForCustomerDto> productVariation) {

}
