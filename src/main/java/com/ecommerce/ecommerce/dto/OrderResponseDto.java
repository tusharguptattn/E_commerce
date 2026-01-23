package com.ecommerce.ecommerce.dto;

import com.ecommerce.ecommerce.entity.OrderProduct;
import com.ecommerce.ecommerce.entity.embeddable.OrderAddress;
import com.ecommerce.ecommerce.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderResponseDto(BigDecimal amountPaid,
                               String paymentMethod,
                               OrderAddress orderAddress,
                               Long orderId,
                               Date orderDate,
                               List<OrderProductDtoResponse> orderedProducts) {

}
