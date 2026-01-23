package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;

public record OrderProductDtoForSeller(int quantity,
                                       BigDecimal price,
                                       String productName) {

}
