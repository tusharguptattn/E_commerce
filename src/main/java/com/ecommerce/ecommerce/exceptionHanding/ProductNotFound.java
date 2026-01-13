package com.ecommerce.ecommerce.exceptionHanding;

public class ProductNotFound extends RuntimeException{
    public ProductNotFound(String message){
        super(message);
    }
}
