package com.ecommerce.ecommerce.dto;

import java.util.List;

public record MetadataFilterDto(
        String fieldName,
        List<String> values
) {}
