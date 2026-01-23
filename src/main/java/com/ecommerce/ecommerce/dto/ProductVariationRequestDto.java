package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProductVariationRequestDto(
        @NotNull(message = "Product ID cannot be null")
        Long productId,
        @Min(value = 0, message = "Quantity available cannot be negative")
        int quantityAvailable,
        @Min(value = 0, message = "Price cannot be negative")
        BigDecimal price,

        MultipartFile primaryImage,
        List<MultipartFile> secondaryImages,
        @NotEmpty(message = "Metadata cannot be empty")
        String metadata
) {
}
