package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;

public record PriceRangeDto(
        BigDecimal min,
        BigDecimal max
) {}