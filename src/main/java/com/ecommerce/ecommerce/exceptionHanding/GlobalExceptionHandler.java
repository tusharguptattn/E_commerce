package com.ecommerce.ecommerce.exceptionHanding;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<String> handleResourceNotFound(BadRequest ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleInsufficientStockException(BusinessException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMessage);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<String> locked() {
        return ResponseEntity.status(401)
                .body("Account is locked due to multiple failed login attempts. Please contact support.");
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<String> expired() {
        return ResponseEntity.status(401)
                .body("Account Password is expired");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<String> disabled() {
        return ResponseEntity.status(401)
                .body("Account is not activated");
    }
}
