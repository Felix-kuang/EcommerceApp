package com.example.ecommerce.exception;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(Long productId, Long cartId) {
        super("Product not found in cart with id: " + cartId + " for product id: " + productId);
    }
}