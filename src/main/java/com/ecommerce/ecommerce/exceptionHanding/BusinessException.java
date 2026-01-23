package com.ecommerce.ecommerce.exceptionHanding;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
