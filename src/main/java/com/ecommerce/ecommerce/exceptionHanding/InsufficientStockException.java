package com.ecommerce.ecommerce.exceptionHanding;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
