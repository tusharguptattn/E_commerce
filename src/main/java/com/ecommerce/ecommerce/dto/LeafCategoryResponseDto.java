package com.ecommerce.ecommerce.dto;

import java.util.List;

public record LeafCategoryResponseDto(
        Long categoryId,
        String categoryName,
        List<CategoryMetadataFieldResponseDto> parentChain,
        List<CategoryMetadataResponseDto> metadata
) {}
