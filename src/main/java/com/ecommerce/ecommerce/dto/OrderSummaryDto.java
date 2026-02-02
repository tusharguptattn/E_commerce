package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.enums.OrderStatus;

public record OrderSummaryDto(
    String sellerEmail,
    long orderId,
    String productName,
    OrderStatus status) {

}
