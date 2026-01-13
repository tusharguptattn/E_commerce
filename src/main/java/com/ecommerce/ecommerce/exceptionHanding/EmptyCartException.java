package com.ecommerce.ecommerce.exceptionHanding;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String message){
        super(message);
    }
}
