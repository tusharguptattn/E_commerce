package com.ecommerce.ecommerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record ProductVariationUpdateRequest(
    @NotNull(message = "Variation id cannot be null")
    Long productVariationId,

    @Min(value = 0, message = "Quantity cannot be negative")
    Integer quantity,

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    BigDecimal price,

    @Size(max = 1000, message = "Metadata must not exceed 1000 characters")
    String metadata,

    MultipartFile primaryImage,          // optional in update

    List<MultipartFile> secondaryImages, // optional in update

    Boolean isActive) {
}
