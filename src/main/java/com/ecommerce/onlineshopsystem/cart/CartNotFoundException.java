package com.ecommerce.onlineshopsystem.cart;


public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
