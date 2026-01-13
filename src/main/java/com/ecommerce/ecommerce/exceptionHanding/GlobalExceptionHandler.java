package com.ecommerce.ecommerce.exceptionHanding;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleUserNameNotFound(UserNotFound ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFound ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CartItemNotFound.class)
    public ResponseEntity<String> handleCartItemNotFound(CartItemNotFound ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AddressNotFound.class)
    public ResponseEntity<String> handleAddressNotFound(AddressNotFound ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(AddressNotBelongToThisUser.class)
    public ResponseEntity<String> handleAddressNotBelongToThisUser(AddressNotBelongToThisUser ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<String> handleEmptyCartException(EmptyCartException ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PaymentNotFound.class)
    public ResponseEntity<String> handlePaymentNotFound(PaymentNotFound ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(OrderNotFound.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFound ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(TransactionsNotMatch.class)
    public ResponseEntity<String> handleTransactionsNotMatch(TransactionsNotMatch ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<String> handleCategoryNotFound(CategoryNotFound ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExist.class)
    public ResponseEntity<String> handleCategoryAlreadyExist(CategoryAlreadyExist ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PaymentFailed.class)
    public ResponseEntity<String> handlePaymentFailed(PaymentFailed ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStockException(InsufficientStockException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
