package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.security.SecurityUtil;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDTO> addToCart(
            @RequestParam String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        if (!SecurityUtil.getLoggedInUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        return ResponseEntity.ok(cartService.addToCart(username, productId, quantity));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CartDTO> getCart(@RequestParam String username) {
        return ResponseEntity.ok(cartService.getCartForUser(username));
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateCartItem(
            @RequestParam String username,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        // Ambil user yang lagi login dari SecurityContextHolder
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        // Cek apakah user yang login sama dengan yang diminta
        if (!username.equals(loggedInUsername)) {
            throw new AccessDeniedException("You can only modify your own cart");
        }

        cartService.updateCartItem(username, productId, quantity);
        return ResponseEntity.ok("Cart updated successfully");
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> removeFromCart(
            @RequestParam String username,
            @RequestParam Long productId) {
        if (!SecurityUtil.getLoggedInUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only modify your own cart");
        }

        cartService.removeFromCart(username, productId);

        return ResponseEntity.ok("Item removed successfully");
    }
}
