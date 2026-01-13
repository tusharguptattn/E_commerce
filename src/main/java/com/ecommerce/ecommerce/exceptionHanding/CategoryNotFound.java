package com.ecommerce.ecommerce.exceptionHanding;

public class CategoryNotFound extends RuntimeException{
    public CategoryNotFound(String message){
        super(message);
    }
}
