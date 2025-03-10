package com.example.ecommerce.exception;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(Long productId, Long cartId) {
        super("Product with id: " + productId + " not found at cart with id: " + cartId);
    }
}
