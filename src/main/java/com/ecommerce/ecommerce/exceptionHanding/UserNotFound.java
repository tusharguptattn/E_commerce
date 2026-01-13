package com.ecommerce.ecommerce.exceptionHanding;

public class UserNotFound extends RuntimeException{
    public UserNotFound(String message){
        super(message);
    }
}
