package com.ecommerce.ecommerce.dto;

import java.math.BigDecimal;

public record OrderProductDtoResponse (BigDecimal price,
                                       int quantity,
                                       String name){

}
