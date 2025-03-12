package com.example.ecommerce.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User not Found");
    }
}
