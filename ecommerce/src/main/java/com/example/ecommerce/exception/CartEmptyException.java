package com.example.ecommerce.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(){
        super("Cart is empty");
    }
}
