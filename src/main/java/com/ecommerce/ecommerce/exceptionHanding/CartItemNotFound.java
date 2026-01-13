package com.ecommerce.ecommerce.exceptionHanding;

public class CartItemNotFound extends RuntimeException{
    public CartItemNotFound(String message){
        super(message);
    }
}
