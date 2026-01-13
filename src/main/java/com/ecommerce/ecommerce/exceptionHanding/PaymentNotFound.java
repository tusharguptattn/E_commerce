package com.ecommerce.ecommerce.exceptionHanding;

public class PaymentNotFound extends RuntimeException{
    public PaymentNotFound(String message){
        super(message);
    }
}
