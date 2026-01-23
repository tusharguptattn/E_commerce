package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductVariationResponseForCustomerDto(Long productVariationid,
                                                     int quantity,
                                                     BigDecimal price,
                                                     Map<String,Object> metedata,
                                                     String primaryImage,
                                                     boolean isActive,
                                                     boolean outOfStock) {

}
