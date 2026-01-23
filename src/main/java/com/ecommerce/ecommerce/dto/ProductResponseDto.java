package com.ecommerce.ecommerce.dto;

public record ProductResponseDto(
        Long id,
        String name,
        String brand,
        ChildCategoryResponseDto category,
        String description){
}
