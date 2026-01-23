package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariationResponseWithoutProductDetailsDto(Long id,
                                                               Map<String,Object> metadata,
                                                               int stock,
                                                               BigDecimal price) {
}
