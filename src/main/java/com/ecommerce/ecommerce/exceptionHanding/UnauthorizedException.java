package com.ecommerce.ecommerce.exceptionHanding;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
