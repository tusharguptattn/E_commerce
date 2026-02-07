package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.enums.OrderStatus;
import java.math.BigDecimal;

public record AdminOrderResponse(
    Long orderId,
    String customerEmail,
    String customerName,
    BigDecimal totalAmount
) {}
