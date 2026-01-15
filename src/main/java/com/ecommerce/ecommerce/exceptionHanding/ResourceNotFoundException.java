package com.ecommerce.ecommerce.exceptionHanding;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
