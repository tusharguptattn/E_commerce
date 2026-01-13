package com.ecommerce.ecommerce.exceptionHanding;

public class OrderNotFound extends RuntimeException{
    public OrderNotFound(String message){
        super(message);
    }
}
