package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record ProductVariationUpdateRequest(
        @NotNull(message = "variation id can not be null")
        Long productVariationid,
        int quantity,
        BigDecimal price,
        Map<String,Object> metedata,
        MultipartFile primaryImage,
        List<MultipartFile> secondaryImages,
        boolean isActive) {
}
