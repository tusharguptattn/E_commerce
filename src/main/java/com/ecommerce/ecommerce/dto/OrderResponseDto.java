package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponseDto(Long orderId,
                                double totalAmount,
                               OrderStatus status,
                               List<OrderItemResponseDto> items) {
}
