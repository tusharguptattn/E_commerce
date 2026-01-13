package com.ecommerce.ecommerce.exceptionHanding;

public class AddressNotFound extends RuntimeException{
    public AddressNotFound(String message){
        super(message);
    }
}
