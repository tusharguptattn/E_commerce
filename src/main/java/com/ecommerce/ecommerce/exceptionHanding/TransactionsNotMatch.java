package com.ecommerce.ecommerce.exceptionHanding;

public class TransactionsNotMatch extends RuntimeException{
    public TransactionsNotMatch(String message){
        super(message);
    }
}
