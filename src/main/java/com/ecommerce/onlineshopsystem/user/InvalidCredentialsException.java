package com.ecommerce.onlineshopsystem.user;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
