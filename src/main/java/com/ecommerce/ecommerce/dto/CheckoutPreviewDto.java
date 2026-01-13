package com.ecommerce.ecommerce.dto;

import java.util.List;

public record CheckoutPreviewDto(
        double totalAmount,
        List<OrderItemResponseDto> items
) {}
