package com.ecommerce.ecommerce.exceptionHanding;

public class PaymentFailed extends RuntimeException{
    public PaymentFailed(String message) {
        super(message);
    }
}
