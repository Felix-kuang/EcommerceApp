package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.ecommerce.dto.UpdateCartItemDTO;
import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.dto.RemoveFromCartDTO;
import com.example.ecommerce.service.CartService;

import jakarta.validation.Valid;

import com.example.ecommerce.security.SecurityUtil;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDTO> addToCart(@RequestBody @Valid UpdateCartItemDTO request) {

        if (!SecurityUtil.getLoggedInUsername().equals(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        return ResponseEntity
                .ok(cartService.addToCart(request.getUsername(), request.getProductId(), request.getQuantity()));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDTO> getCart(@RequestBody String username) {
        return ResponseEntity.ok(cartService.getCartForUser(username));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCartItem(
            @RequestBody UpdateCartItemDTO request) {

        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!request.getUsername().equals(loggedInUsername)) {
            throw new AccessDeniedException("You can only modify your own cart");
        }

        cartService.updateCartItem(request.getUsername(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Cart updated successfully");
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> removeFromCart(
            @RequestBody @Valid RemoveFromCartDTO request) {
        if (!SecurityUtil.getLoggedInUsername().equals(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only modify your own cart");
        }

        cartService.removeFromCart(request.getUsername(), request.getProductId());

        return ResponseEntity.ok("Item removed successfully");
    }
}
