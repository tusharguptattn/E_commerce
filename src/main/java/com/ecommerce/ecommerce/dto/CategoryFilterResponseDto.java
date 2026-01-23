package com.ecommerce.ecommerce.dto;

import java.util.List;

public record CategoryFilterResponseDto(
        List<MetadataFilterDto> metadataFilters,
        List<String> brands,
        PriceRangeDto priceRange
) {}