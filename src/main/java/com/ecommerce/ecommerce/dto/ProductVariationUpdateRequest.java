package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ProductVariationUpdateRequest(
        @NotNull(message = "variation id can not be null")
        Long productVariationid,
        Integer quantity,
        BigDecimal price,
        String metedata,
        MultipartFile primaryImage,
        List<MultipartFile> secondaryImages,
        Boolean isActive) {
}
