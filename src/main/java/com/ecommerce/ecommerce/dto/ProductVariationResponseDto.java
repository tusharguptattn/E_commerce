package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariationResponseDto(
        Long id,
        Map<String,Object> metadata,
        int stock,
        BigDecimal price,
        ProductResponseDto product) {
}
