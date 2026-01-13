package com.ecommerce.ecommerce.exceptionHanding;

public class AddressNotBelongToThisUser extends RuntimeException{
    public AddressNotBelongToThisUser(String message){
        super(message);
    }
}
