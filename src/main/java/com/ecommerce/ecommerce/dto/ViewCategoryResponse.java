package com.ecommerce.ecommerce.dto;

import java.util.List;

public record ViewCategoryResponse(
        Long id,
        String name,
        List<ParentCategoryResponseDto> parentHierarchy,
        List<ChildCategoryResponseDto> childCategories
) {}
