package com.ecommerce.ecommerce.exceptionHanding;

public class CategoryAlreadyExist extends RuntimeException{
    public CategoryAlreadyExist(String message) {
        super(message);
    }
}
